package hu.elte.szgy.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
	private Osztaly osztaly;
	
	public Osztaly getOsztaly()
	{
		return osztaly;
	}
	public void setOsztaly( Osztaly osztaly )
	{
		this.osztaly = osztaly;
	}

}

