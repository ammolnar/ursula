package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="osztaly")
public class Osztaly implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
	private String nev;

	@ManyToOne
	@JoinColumn(nullable = true)
	private Orvos vezeto;

	@ManyToOne
	@JoinColumn(nullable = true)
	private Orvos ugyeletes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "osztaly")
    @JsonIgnore
    private Set<Orvos> orvosok = new HashSet<Orvos>(0);
    
	public String getNev() {
		return nev;
	}

	public void setNev( String nev ) {
		this.nev = nev;
	}

	public Orvos getVezeto() {
		return vezeto;
	}

	public void setVezeto( Orvos vezeto )
	{
		this.vezeto = vezeto;
	}

	public Orvos getUgyeletes()
	{
		return ugyeletes;
	}

	public void setUgyeletes( Orvos ugyeletes )
	{
		this.ugyeletes = ugyeletes;
	}

	public Set<Orvos> getOrvosok()
	{
		return orvosok;
	}

	public void setOrvosok( Set<Orvos> orvosok )
	{
		this.orvosok = orvosok;
	}

	public Osztaly() {
		;
	}
    
	public Osztaly(String n) {
		nev = n;
	}
}

