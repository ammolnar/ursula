package hu.elte.szgy.rest;

import hu.elte.szgy.data.Ellato;
import hu.elte.szgy.data.EllatoRepository;
import hu.elte.szgy.data.Labor;
import hu.elte.szgy.data.Orvos;
import hu.elte.szgy.data.OrvosRepository;
import hu.elte.szgy.data.Osztaly;
import hu.elte.szgy.data.OsztalyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ellato")
@Transactional
public class EllatoManager {
    private static Logger log = LoggerFactory.getLogger(BetegManager.class);
    @Autowired
    private EllatoRepository ellaDao;

    @Autowired
    private OrvosRepository orvDao;

    @Autowired
    private OsztalyRepository osztDao;

    @GetMapping("/all")
    public ResponseEntity<List<Ellato>> getAllEllato(@RequestParam(name="type",required=false) String type) {
	List<Ellato> elllist = ellaDao.findAll();
	if(type != null) {
	    List<Ellato> fulllist = elllist;
	    elllist = new ArrayList<Ellato>();
	    for(Ellato e : fulllist) if(e.getType().equals(type)) elllist.add(e);
	   
/*	    ListIterator<Ellato> lii = elllist.listIterator();
	    while(lii.hasNext()) if(!lii.next().getType().equals(type)) lii.remove();*/
	}
	return new ResponseEntity<List<Ellato>>(elllist, HttpStatus.OK);
	 
    }

    @GetMapping("/{elid}")
    public ResponseEntity<Ellato> getEllato(@PathVariable("elid") Integer elid) {
	Ellato ella = ellaDao.findById(elid).get();
	log.debug("ELLATO identified: " + ella.getClass().getName() + "Equals Orvos: "
		+ (ella.getClass() == Orvos.class));
	return new ResponseEntity<Ellato>(ella, HttpStatus.OK);
    }

    @GetMapping("/byname/{name}")
    public ResponseEntity<Ellato> getNamedEllato(@PathVariable("name") String nev) {
	return getEllato(ellaDao.findByNev(nev).getElid());
    }

    @GetMapping("/osztaly/{oszid}")
    public ResponseEntity<Osztaly> getOsztalyByName(@PathVariable("oszid") String oszt_nev) {
	Osztaly o = osztDao.findById(oszt_nev).get();
	// Osztaly o = osztDao.getOne(oszt_nev);  THIS DOES NOT WORK. RETURNS OBJECT REFERENCE  
	return new ResponseEntity<Osztaly>(o, HttpStatus.OK);
    }

    @GetMapping("/osztaly/all")
    public ResponseEntity<List<Osztaly>> getAllOsztaly() {
	return new ResponseEntity<List<Osztaly>>(osztDao.findAll(), HttpStatus.OK);
    }

    @GetMapping("/osztaly/{oszid}/orvosok")
    public ResponseEntity<List<Orvos>> getOrvosokByOsztaly(@PathVariable("oszid") String oszt_nev) {
	Osztaly oszi = osztDao.getOne(oszt_nev);
	return new ResponseEntity<List<Orvos>>(orvDao.findOrvosByOsztaly(oszi), HttpStatus.OK);
    }

    @PostMapping("/orvos/new")
    public ResponseEntity<Orvos> createOrvos(@RequestBody Orvos b) {
	if(b.getOsztalyId() != null) b.setOsztaly(osztDao.findById(b.getOsztalyId()).get());
	orvDao.save(b);
	return new ResponseEntity<Orvos>(b, HttpStatus.CREATED);
    }

    @PostMapping("/orvos/{orvid}")
    public ResponseEntity<Orvos> updateOrvos(@PathVariable("orvid") String orvid, @RequestBody Orvos b) {
	if(b.getElid() != Integer.parseInt(orvid)) {
	    throw new AccessDeniedException("Cannot save on different object");
	}
	Orvos orv = orvDao.findById(Integer.parseInt(orvid)).get();
	if(b.getOsztalyId() != null) {
	    if(b.getOsztalyId().equals("none")) orv.setOsztaly(null); 
	    else orv.setOsztaly(osztDao.findById(b.getOsztalyId()).get());
	}
	if(b.getNev() != null) {
	    orv.setNev(b.getNev());
	}
	orvDao.save(orv);
	return new ResponseEntity<Orvos>(b, HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/labor/new")
    public ResponseEntity<Labor> createLabor(@RequestBody Labor b) {
	ellaDao.save(b);
	return new ResponseEntity<Labor>(b, HttpStatus.CREATED);
    }

    void setOsztalyRelations(Osztaly o, Integer vezid, Integer ugyid) {
	Ellato ella;
	if (vezid != null) {
	    ella = ellaDao.findById(vezid).get();
	    if (!ella.getType().equals("ORVOS"))
		throw new IllegalArgumentException("Ellato specified is not an Orvos");
	    o.setVezeto((Orvos) ella);
	} else if (ugyid == null)
	    o.setUgyeletes(null);
	if (ugyid != null) {
	    if (ugyid == -1)
		o.setUgyeletes(null);
	    else {
		ella = ellaDao.findById(ugyid).get();
		if (!ella.getType().equals("ORVOS"))
		    throw new IllegalArgumentException("Ellato specified is not an Orvos");
		o.setUgyeletes((Orvos) ella);
	    }
	}
    }

    @PostMapping("/osztaly/new")
    public ResponseEntity<Osztaly> createOsztaly(@RequestBody Osztaly osz) {
	setOsztalyRelations(osz, osz.getVezetoId(), osz.getUgyeletesId());
	osztDao.save(osz);
	return new ResponseEntity<Osztaly>(osz, HttpStatus.CREATED);
    }

    @PostMapping("/osztaly/{oszid}")
    public ResponseEntity<Void> saveOsztaly(@PathVariable("oszid") String oszid, @RequestBody Osztaly osznew) {
	if (!osznew.getNev().equals(oszid))
	    throw new AccessDeniedException("Cannot save on different object");
	Osztaly osz = osztDao.findById(oszid).get();
	setOsztalyRelations(osz, osznew.getVezetoId(), osznew.getUgyeletesId());
	osztDao.save(osz);
	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/osztaly/{oszid}/ugyeletes")
    public ResponseEntity<Void> saveOsztalyUgyeletes(@PathVariable("oszid") String oszid, @RequestBody Integer orvi) {
	setOsztalyRelations(osztDao.getOne(oszid), null, orvi);
	return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    /*
     * osztaly/<oid> R A L O osztaly/<oid>/orvosok R A L O
     * 
     * P ellato/new A P osztaly/new A P ellato/<elid> A ; oszt�lyv�lt�s is P
     * osztaly/<oid> A ; vezet� �s �gyeletesvaltas is P osztaly/<oid>/ugyeletes A ->
     * csak �gyeltesv�lt�s
     */
}
