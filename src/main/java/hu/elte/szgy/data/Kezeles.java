package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="kezeles")
public class Kezeles implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int kezid;

	@ManyToOne
	@JoinColumn(name = "fk_esid", nullable = false)
	private Eset eset;

	@ManyToOne
	@JoinColumn(name = "fk_elid", nullable = false)
	private Ellato ellato;

    enum Statusz{ ELOJEGYZETT, NYITOTT, LEZART }

    @Enumerated(EnumType.ORDINAL)
    private Statusz statusz;

    private String specifikacio;

    private String nyitallapot;

    private String vegallapot;

    private String info;

    private int tajpontok;

	private Date nyitdate;

	private Date vegdate;

    public int getKezid() { return this.kezid; }
    public void setKezid(int kezid) { this.kezid = kezid; }
    public Eset getEset() { return this.eset; }
    public void setEset(Eset eset) { this.eset = eset; }
    public Ellato getEllato() { return this.ellato; }
    public void setEllato(Ellato ellato) { this.ellato = ellato; }
    public Statusz getStatusz() { return this.statusz; }
    public void setStatusz(Statusz statusz) { this.statusz = statusz; }
    public String getSpecifikacio() { return this.specifikacio; }
    public void setSpecifikacio(String specifikacio) { this.specifikacio = specifikacio; }
    public String getNyitallapot() { return this.nyitallapot; }
    public void setNyitallapot(String nyitallapot) { this.nyitallapot = nyitallapot; }
    public String getVegallapot() { return this.vegallapot; }
    public void setVegallapot(String vegallapot) { this.vegallapot = vegallapot; }
    public String getInfo() { return this.info; }
    public void setInfo(String info) { this.info = info; }
    public int getTajpontok() { return this.tajpontok; }
    public void setTajpontok(int tajpontok) { this.tajpontok = tajpontok; }
    public Date getNyitdate() { return this.nyitdate; }
    public void setNyitdate(Date nyitdate) { this.nyitdate = nyitdate; }
    public Date getVegdate() { return this.vegdate; }
    public void setVegdate(Date vegdate) { this.vegdate = vegdate; }

}

