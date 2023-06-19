package it.polito.tdp.itunes.model;

public class CoppiaA {

	private Track t1;
	private Track t2;
	private Double peso;
	
	public CoppiaA(Track t1, Track t2, Double peso) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.peso = peso;
	}
	public Track getT1() {
		return t1;
	}
	public void setT1(Track t1) {
		this.t1 = t1;
	}
	public Track getT2() {
		return t2;
	}
	public void setT2(Track t2) {
		this.t2 = t2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	
}
