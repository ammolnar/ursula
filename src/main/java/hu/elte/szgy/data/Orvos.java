package hu.elte.szgy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="orvos")
@PrimaryKeyJoinColumn(name = "elid")
public class Orvos extends Ellato implements Serializable { 

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(nullable = true)
	@JsonIgnore
	private Osztaly osztaly;
	
	@Column(name = "osztaly_nev", insertable = false, updatable = false)
	private String osztalyId;
	
	public Osztaly getOsztaly()
	{
		return osztaly;
	}
	public void setOsztaly( Osztaly osztaly )
	{
		this.osztaly = osztaly;
	}
	public String getOsztalyId()
	{
		return osztalyId;
	}
	public void setOsztalyId( String osztalyId )
	{
		this.osztalyId = osztalyId;
	}

}

