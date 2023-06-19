package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.CoppiaA;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Track> getAllTracks(Genre g) {
		final String sql = "SELECT * "
				+ "FROM track t "
				+ "WHERE t.GenreId = ? ";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, g.getGenreId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<CoppiaA> getAllCoppie(Genre g, Map<Integer, Track>idMapTracks) {
		final String sql = "SELECT t1.TrackId AS id1, t2.TrackId AS id2 "
				+ "FROM track t1, track t2 "
				+ "WHERE t1.GenreId = ? "
				+ "AND t2.GenreId = ? "
				+ "AND t1.MediaTypeId = t2.MediaTypeId "
				+ "AND t1.TrackId < t2.TrackId ";
		List<CoppiaA> result = new ArrayList<CoppiaA>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, g.getGenreId());
			st.setInt(2, g.getGenreId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
			    Integer t1ID = res.getInt("id1");
			    Integer t2ID = res.getInt("id2");
			    Double peso = getPeso(t1ID, t2ID);
			    Track t1 = idMapTracks.get(t1ID);
			    Track t2 = idMapTracks.get(t2ID);
			    CoppiaA c = new CoppiaA(t1, t2, peso);
			    result.add(c);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	private Double getPeso(Integer t1id, Integer t2id) {
		final String sql = "SELECT SUM(t1.Milliseconds-t2.Milliseconds)AS peso "
				+ "FROM track t1, track t2 "
				+ "WHERE t1.TrackId = ? "
				+ "AND t2.TrackId = ? ";
		Double peso = 0.0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t1id);
			st.setInt(2, t2id);
			ResultSet res = st.executeQuery();

			if (res.first()) {
				if(res.getDouble("peso")>=0) {
					peso = res.getDouble("peso");
				}else {
					peso = -res.getDouble("peso");
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return peso;
	}

	
	
}
