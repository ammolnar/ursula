package hu.elte.szgy.data;


import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="labor")
@PrimaryKeyJoinColumn(name = "elid")
public class Labor extends Ellato { 

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String telefon;
	public String getTelefon()
	{
		return telefon;
	}
	public void setTelefon( String telefon )
	{
		this.telefon = telefon;
	}

}

