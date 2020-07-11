package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Graph<Director, DefaultWeightedEdge> grafo2;
	private Map<Integer, Director> mappa;
	
	public Model() {
		dao = new ImdbDAO();
		mappa = new HashMap<Integer, Director>();
	}
	
	public void riempiMappa(Integer anno) {
		mappa.clear();
		dao.loadAllDirectors(mappa, anno);
	}
	
	public List<Integer> getAnno(){
		List<Integer> risultato = new LinkedList<Integer>();
		risultato.add(2004);
		risultato.add(2005);
		risultato.add(2006);
		return risultato;
	}
	//con interi
	/*
	public void creaGrafo(Integer anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(anno));
		
		for(Coppia c : dao.getArchi(anno)) {
			if(!grafo.containsEdge(c.getD1(), c.getD2())) {
				Graphs.addEdgeWithVertices(grafo, c.getD1(), c.getD2(), c.getPeso());
			}
		}
	}
	*/
	
	public void creaGrafo(Integer anno) {
		
		grafo2 = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo2, mappa.values());
		
		for(Coppia2 c : dao.getArchiVersione2(mappa, anno)) {
			if(!grafo2.containsEdge(c.getD1(), c.getD2())) {
				Graphs.addEdgeWithVertices(grafo2, c.getD1(), c.getD2(), c.getPeso());
			}
		}
	}
	
	public Integer nVert() {
		return this.grafo2.vertexSet().size();
	}
	
	public Integer nArchi() {
		return this.grafo2.edgeSet().size();
	}
	
	public List<Director> getDirettori(){
		List<Director> dir = new ArrayList<>();
		for (Director d : mappa.values()) {
			dir.add(d);
		}
		Collections.sort(dir);
		return dir;
	}
	
	public List<Vicino> getVicini(Director d){
		List<Vicino> vicini = new ArrayList<>();
		List<Director> dir = Graphs.neighborListOf(grafo2, d);
		for(Director direttore : dir) {
			vicini.add(new Vicino(direttore, (int)grafo2.getEdgeWeight(grafo2.getEdge(direttore, d))));
		}
		Collections.sort(vicini);
		return vicini;
	}
}
