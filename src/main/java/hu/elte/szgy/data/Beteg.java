package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="beteg")
@NamedQuery(name="Beteg.hasPendingKezelesBy", query="SELECT count(*) from Kezeles k WHERE k.eset.beteg.taj=:taj AND k.ellato.elid=:elid") 
public class Beteg implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
	private int taj;

    private String nev;

	private Date szuldatum;

    private String foglalkozas;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "beteg")
	private Set<Eset> esetek = new HashSet<Eset>(0);

	public int getTaj() { return taj; }
	public void setTaj(int taj) { this.taj = taj; }
	public String getNev() { return nev; }
	public void setNev(String nev) { this.nev = nev; }
	public Date getSzuldatum() { return szuldatum; }
	public void setSzuldatum(Date szuldatum) { this.szuldatum = szuldatum; }
	public String getFoglalkozas() { return foglalkozas; }
	public void setFoglalkozas(String foglalkozas) { this.foglalkozas = foglalkozas; }

	public Set<Eset> getEsetek() {
		return this.esetek;
	}

	public void setEsetek(Set<Eset> esetek) {
		this.esetek = esetek;
	}


    public Beteg() { ; }
    public Beteg(Integer taj) { this.taj = taj; }}

