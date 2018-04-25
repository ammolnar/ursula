package hu.elte.szgy.rest;

import hu.elte.szgy.data.Ellato;
import hu.elte.szgy.data.EllatoRepository;
import hu.elte.szgy.data.Labor;
import hu.elte.szgy.data.Orvos;
import hu.elte.szgy.data.OrvosRepository;
import hu.elte.szgy.data.Osztaly;
import hu.elte.szgy.data.OsztalyRepository;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ellato")
@Transactional
public class EllatoManager
{
	private static Logger log = LoggerFactory.getLogger(BetegManager.class);
	@Autowired
	private EllatoRepository ellaDao;
	
	@Autowired
	private OrvosRepository orvDao;

	@Autowired
	private OsztalyRepository osztDao;

	@GetMapping("/all")
    public ResponseEntity<List<Ellato>>  getAllEllato() {
		return new ResponseEntity<List<Ellato>>(ellaDao.findAll(),HttpStatus.OK);
	}
	
    @GetMapping("/{elid}")
    public ResponseEntity<Ellato>  getEllato(@PathVariable("elid") Integer elid) {
		Ellato ella = ellaDao.findById(elid).get();
    	log.debug("ELLATO identified: " + ella.getClass().getName() + "Equals Orvos: " + (ella.getClass() == Orvos.class));
		return new ResponseEntity<Ellato>(ella, HttpStatus.OK);
	}

    @GetMapping("/byname/{name}")
    public ResponseEntity<Ellato>  getNamedEllato(@PathVariable("name") String nev) {
    	return getEllato(ellaDao.findByNev(nev).getElid());
    }

    @GetMapping("/osztaly/{oszid}")
    public ResponseEntity<Osztaly>  getOsztalyByName(@PathVariable("oszid") String oszt_nev) {
    	return new ResponseEntity<Osztaly>(osztDao.getOne(oszt_nev), HttpStatus.OK);
    }

    @GetMapping("/osztaly/all")
    public ResponseEntity<List<Osztaly>>  getAllOsztaly() {
    	return new ResponseEntity<List<Osztaly>>(osztDao.findAll(), HttpStatus.OK);
    }

    @GetMapping("/osztaly/{oszid}/orvosok")
    public ResponseEntity<List<Orvos>>  getOrvosokByOsztaly(@PathVariable("oszid") String oszt_nev) {
    	Osztaly oszi = osztDao.getOne(oszt_nev);
    	return new ResponseEntity<List<Orvos>>(orvDao.findOrvosByOsztaly(oszi), HttpStatus.OK);
    }

    @PostMapping("/orvos/new")
    public ResponseEntity<Orvos>  createOrvos(@RequestBody Orvos b) {
    	orvDao.save(b);
    	return new ResponseEntity<Orvos>(b, HttpStatus.CREATED);
    }

    @PostMapping("/labor/new")
    public ResponseEntity<Labor>  createLabor(@RequestBody Labor b) {
    	ellaDao.save(b);
    	return new ResponseEntity<Labor>(b, HttpStatus.CREATED);
    }

    @PostMapping("/osztaly/new")
    public ResponseEntity<Void>  createOsztaly(@RequestBody Osztaly osz) {
    	osztDao.save(osz);
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PostMapping("/osztaly/{oszid}")
    public ResponseEntity<Void>  saveOsztaly(@PathVariable("oszid") String oszid, @RequestBody Osztaly osz) {
    	if(!osz.getNev().equals( oszid )) throw new AccessDeniedException( "Cannot save on different object" );
    	osztDao.save(osz);
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PostMapping("/osztaly/{oszid}/ugyletes")
    public ResponseEntity<Void>  saveOsztalyUgyeletes(@PathVariable("oszid") String oszid, @RequestBody Orvos orvi) {
    	osztDao.getOne( oszid ).setUgyeletes(orvi);	
    	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    /*
	osztaly/<oid>      R A L O
	osztaly/<oid>/orvosok R A L O

	P ellato/new     A
	P osztaly/new	 A
	P ellato/<elid>  A ; oszt�lyv�lt�s is
	P osztaly/<oid>  A ; vezet� �s �gyeletesvaltas is
	P osztaly/<oid>/ugyeletes A -> csak �gyeltesv�lt�s
*/
}
