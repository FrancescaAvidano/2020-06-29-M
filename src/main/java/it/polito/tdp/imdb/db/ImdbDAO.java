package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Coppia;
import it.polito.tdp.imdb.model.Coppia2;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void loadAllDirectors(Map<Integer, Director> idMap, Integer anno){
		String sql = "SELECT DISTINCT d.id AS id, d.first_name AS first_name, d.last_name AS last_name "+
				"FROM movies AS m, movies_directors AS md, directors AS d "+
				"WHERE m.id = md.movie_id " +
				"	AND d.id = md.director_id "+
				"AND m.year = ? ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				idMap.put(director.getId(), director);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Integer> getVertici(Integer anno){
		String sql = "SELECT DISTINCT md.director_id AS id " + 
				"FROM movies AS m, movies_directors AS md " + 
				"WHERE m.id = md.movie_id " + 
				"	AND m.year = ? ";
		List<Integer> direttori = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				direttori.add(res.getInt("id"));
			}
			conn.close();
			return direttori;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Coppia> getArchi(Integer anno){
		String sql = "SELECT DISTINCT md1.director_id AS d1, md2.director_id AS d2, COUNT(DISTINCT(r1.actor_id)) AS peso " + 
				"FROM movies_directors AS md1, movies_directors AS md2, roles AS r1, roles AS r2, movies AS m1, movies AS m2 " + 
				"WHERE md1.movie_id = r1.movie_id " + 
				"	AND md2.movie_id = r2.movie_id " + 
				"	AND r1.actor_id = r2.actor_id " + 
				"	AND md1.director_id > md2.director_id " + 
				"	AND m1.id = md1.movie_id " + 
				"	AND m2.id = md2.movie_id " + 
				"	AND m1.year = ? " + 
				"	AND m2.year = ? "+ 
				"GROUP BY d1,d2 ";
		List<Coppia> direttori = new ArrayList<Coppia>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				direttori.add(new Coppia(res.getInt("d1"),res.getInt("d2"), res.getInt("peso")));
			}
			conn.close();
			return direttori;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Coppia2> getArchiVersione2(Map<Integer, Director> mappa, Integer anno){
		String sql = "SELECT DISTINCT md1.director_id AS d1, md2.director_id AS d2, COUNT(DISTINCT(r1.actor_id)) AS peso " + 
				"FROM movies_directors AS md1, movies_directors AS md2, roles AS r1, roles AS r2, movies AS m1, movies AS m2 " + 
				"WHERE md1.movie_id = r1.movie_id " + 
				"	AND md2.movie_id = r2.movie_id " + 
				"	AND r1.actor_id = r2.actor_id " + 
				"	AND md1.director_id > md2.director_id " + 
				"	AND m1.id = md1.movie_id " + 
				"	AND m2.id = md2.movie_id " + 
				"	AND m1.year = ? " + 
				"	AND m2.year = ? "+ 
				"GROUP BY d1,d2 ";
		List<Coppia2> direttori = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Director d1 = mappa.get(res.getInt("d1"));
				Director d2 = mappa.get(res.getInt("d2"));
				
				if(d1==null || d2==null) {
					throw new RuntimeException("Problema in getArchiVersione2");
				}
				
				direttori.add(new Coppia2(d1, d2, res.getInt("peso")));
			}
			conn.close();
			return direttori;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
}
