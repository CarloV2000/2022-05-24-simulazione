package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private List<Genre>allGenres;
	private Map<String, Genre>nameGenresMap;
	private Graph<Track, DefaultWeightedEdge>grafo;
	private List<Track>allTracks;
	private ItunesDAO dao;
	private Map<Integer, Track>idMapTracks;
	private Map<String, Track>nameMapTracks;
	
	private List<Track>playlistMigliore;
	private int dimensioneMassima;
	
	public Model() {
		this.dao = new ItunesDAO();
		this.allGenres = new ArrayList<>(dao.getAllGenres());
		this.idMapTracks = new HashMap<>();
		this.nameGenresMap = new HashMap<>();
		this.nameMapTracks = new HashMap<>();
		for(Genre x : allGenres) {
			this.nameGenresMap.put(x.getName(), x);
		}
	}
	public String creaGrafo(String genreNAME) {
		Genre g = this.nameGenresMap.get(genreNAME);
		//grafo
		this.grafo = new SimpleWeightedGraph<Track, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//vertici
		this.allTracks = dao.getAllTracks(g);
		Graphs.addAllVertices(grafo, this.allTracks);
		//idMapTracce
		for(Track t : this.allTracks) {
			this.idMapTracks.put(t.getTrackId(), t);
		}
		//archi
		List<CoppiaA>allCoppie = new ArrayList<>(dao.getAllCoppie(g, idMapTracks));
		for(CoppiaA c : allCoppie) {
			Graphs.addEdge(grafo, c.getT1(), c.getT2(), c.getPeso());
		}
		
		//metto i vertici nella TrackNameMap
		for(Track t : grafo.vertexSet()) {
			this.nameMapTracks.put(t.getName(), t);
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi";
	}
	
	public CoppiaA trovaArcoPiuPesante(String genreNAME){
		Genre g = this.nameGenresMap.get(genreNAME);
		CoppiaA coppia = null;
		double deltaMax = 0.0;
		for(CoppiaA x : dao.getAllCoppie(g, idMapTracks)) {
			if(x.getPeso() > deltaMax) {
				deltaMax = x.getPeso();
			    coppia = x;
			}
		}
		return coppia;
	}
	
	/**
	 * Dato il grafo costruito al punto precedente, si vuole definire un dream team costituito dal più grande numero
	 * di giocatori che non ha giocato nella stessa squadra per l’anno selezionato. La direzione non bada a spese, ed
	 * ha deciso di investire tutto il denaro necessario per assicurarsi i giocatori migliori.
	 *	 Due giocatori possono far parte del dream team se non hanno fatto parte della stessa squadra per l’anno
	 *  	in corso (anche per parte di esso).
	 *   La squadra selezionata dovrà selezionare, fra le varie soluzioni possibili, quella con salario cumulativo più alto.
	 */
	/**
	 * Metodo che calcola il Dream Team
	 */
	public void  calcolaPlaylist(String trackNAME, int bytesMAX) {
		Track t = this.nameMapTracks.get(trackNAME);
		this.dimensioneMassima = 1;
		this.playlistMigliore = new ArrayList<Track>();
		List<Track> rimanenti = new ArrayList<Track>(this.getConnectedComponent(t));
		List<Track> parziale = new ArrayList<Track>();
		parziale.add(t);
		ricorsione(parziale, rimanenti,t , bytesMAX);
	}
	
	
	
	/**
	 * La ricorsione vera e propria
	 * @param parziale
	 * @param rimanenti
	 */
	private void ricorsione(List<Track> parziale, List<Track> rimanenti, Track t, int bytesMAX){
		
		// Condizione Terminale
		if (rimanenti.isEmpty()) {
			int dimensione = parziale.size();
			if (dimensione > this.dimensioneMassima && parziale.contains(t) && this.nBytes(parziale) > bytesMAX) {
				this.dimensioneMassima = dimensione;
				this.playlistMigliore = new ArrayList<Track>(parziale);
			}
			return;
		}
       	for (Track x : rimanenti) {
 			List<Track> currentRimanenti = new ArrayList<>(rimanenti);
 				parziale.add(x);
 				currentRimanenti.remove(x);
 				ricorsione(parziale, currentRimanenti, t , bytesMAX);
 				parziale.remove(parziale.size()-1);
 		}
	}	
	
	public List<Track> getConnectedComponent(Track t){
		ConnectivityInspector<Track, DefaultWeightedEdge> inspector = new ConnectivityInspector<Track, DefaultWeightedEdge>(this.grafo);
		Set<Track> connessi = inspector.connectedSetOf(t);
		List<Track>result = new ArrayList<>(connessi);
		return result;
	}
	
	public Integer nBytes(List<Track>parziale) {
		Integer n = 0;
		for(Track t : parziale) {
			n += t.getBytes();
		}
		return n;
	}

	public List<Genre> getAllGenres() {
		return allGenres;
	}

	public Graph<Track, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Track> getAllTracks() {
		return allTracks;
	}

	public Map<String, Genre> getNameGenresMap() {
		return nameGenresMap;
	}
	
	public Map<String, Track> getNameMapTracks() {
		return nameMapTracks;
	}
	public List<Track> getPlaylistMigliore() {
		return playlistMigliore;
	}
	public int getDimensioneMassima() {
		return dimensioneMassima;
	}
	
	
	
	
}
