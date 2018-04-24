package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="eset")
public class Eset implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int esid;

	@ManyToOne
	@JoinColumn(name = "fk_taj", nullable = false)
	private Beteg beteg;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eset")
    private Set<Kezeles> kezelesek = new HashSet<Kezeles>(0);

    enum Statusz{ NYITOTT, LEZART }

    enum ZarStat{ GYUGYULT, KRONIKUS, MEGHALT }

    @Enumerated(EnumType.ORDINAL)
    private Statusz statusz;

    private String panasz;

    private String diagnozis;

    private int diagstat;

    private int terites;

	private Date nyitdate;

	private Date zardate;

    @Enumerated(EnumType.ORDINAL)
    private ZarStat zarStat;

    public int getEsid() { return this.esid; }
    public void setEsid(int esid) { this.esid = esid; }
    public Beteg getBeteg() { return this.beteg; }
    public void setBeteg(Beteg beteg) { this.beteg = beteg; }
    public Statusz getStatusz() { return this.statusz; }
    public void setStatusz(Statusz statusz) { this.statusz = statusz; }
    public String getPanasz() { return this.panasz; }
    public void setPanasz(String panasz) { this.panasz = panasz; }
    public String getDiagnozis() { return this.diagnozis; }
    public void setDiagnozis(String diagnozis) { this.diagnozis = diagnozis; }
    public int getDiagstat() { return this.diagstat; }
    public void setDiagstat(int diagstat) { this.diagstat = diagstat; }
    public int getTerites() { return this.terites; }
    public void setTerites(int terites) { this.terites = terites; }
    public Date getNyitdate() { return this.nyitdate; }
    public void setNyitdate(Date nyitdate) { this.nyitdate = nyitdate; }
    public Date getZardate() { return this.zardate; }
    public void setZardate(Date zardate) { this.zardate = zardate; }
    public ZarStat getZarStat() { return this.zarStat; }
    public void setZarStat(ZarStat zarStat) { this.zarStat = zarStat; }
    public Set<Kezeles> getKezelesek() { return this.kezelesek; }
    public void setKezelesek(Set<Kezeles> kezelesek) { this.kezelesek = kezelesek; }

}

