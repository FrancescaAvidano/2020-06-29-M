package it.polito.tdp.imdb.model;

public class Coppia {
	private Integer d1;
	private Integer d2;
	private Integer peso;
	/**
	 * @param d1
	 * @param d2
	 * @param peso
	 */
	public Coppia(Integer d1, Integer d2, Integer peso) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.peso = peso;
	}
	public Integer getD1() {
		return d1;
	}
	public void setD1(Integer d1) {
		this.d1 = d1;
	}
	public Integer getD2() {
		return d2;
	}
	public void setD2(Integer d2) {
		this.d2 = d2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
}
