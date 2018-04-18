package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name="ellato")
@Inheritance(strategy = InheritanceType.JOINED)
public class Ellato implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int elid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ellato")
    private Set<Kezeles> kezelesek = new HashSet<Kezeles>(0);

    private String nev;

    public int getElid() { return this.elid; }
    public void setElid(int elid) { this.elid = elid; }
    public Set<Kezeles> getKezelesek() { return this.kezelesek; }
    public void setKezelesek(Set<Kezeles> kezelesek) { this.kezelesek = kezelesek; }
    public String getNev() { return this.nev; }
    public void setNev(String nev) { this.nev = nev; }
}

