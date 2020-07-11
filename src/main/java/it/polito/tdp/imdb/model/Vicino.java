package it.polito.tdp.imdb.model;

public class Vicino implements Comparable<Vicino>{
	Director d;
	Integer peso;
	/**
	 * @param d
	 * @param peso
	 */
	public Vicino(Director d, Integer peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Vicino v) {
		return -this.peso.compareTo(v.getPeso());
	}
	@Override
	public String toString() {
		return d + " # attori condivisi: " + peso;
	}
	
}
