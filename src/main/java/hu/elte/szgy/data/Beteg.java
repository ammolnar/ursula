package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name="beteg")
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

}

