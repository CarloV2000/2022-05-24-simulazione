package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
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
	
	public Model() {
		this.dao = new ItunesDAO();
		this.allGenres = new ArrayList<>(dao.getAllGenres());
		this.idMapTracks = new HashMap<>();
		this.nameGenresMap = new HashMap<>();
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
	
	
	
}
