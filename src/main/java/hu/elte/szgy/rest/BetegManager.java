package hu.elte.szgy.rest;

import hu.elte.szgy.data.Beteg;
import hu.elte.szgy.data.BetegRepository;
import hu.elte.szgy.data.EllatoRepository;
import hu.elte.szgy.data.Eset;
import hu.elte.szgy.data.EsetRepository;
import hu.elte.szgy.data.Kezeles;
import hu.elte.szgy.data.KezelesRepository;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("beteg")
@Transactional
public class BetegManager {
	private static Logger log = LoggerFactory.getLogger(BetegManager.class);

	@Autowired
	private BetegRepository betegRepo;
	@Autowired
	private EsetRepository esetRepo;
	@Autowired
	private KezelesRepository kezelesRepo;
	@Autowired
	private EllatoRepository ellatoRepo;

	int userUrsulaID(Authentication auth) {
		return ((UrsulaUserPrincipal)auth.getPrincipal()).getUrsulaId();
	}
	
	@GetMapping("/self")
	public ResponseEntity<Beteg> selfBeteg(Authentication auth) {
		 return new ResponseEntity<Beteg>(betegRepo.findById(userUrsulaID(auth)).get(), HttpStatus.OK);
	}
	
	@GetMapping("/{taj}")
        public ResponseEntity<Beteg> findBeteg(@PathVariable("taj") Integer taj, 
    		Principal principal, Authentication auth) {

		Beteg b = betegRepo.findById(taj).get();
//		if(b == null) throw new ResourceNotFoundException( "Beteg "+taj+" not found"  );
		if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ELLATO"))) {
			if(betegRepo.hasPendingKezelesBy(taj,userUrsulaID(auth))==0) throw new AccessDeniedException("No access to TAJ: "+taj);
			log.debug( "Checked uses for orvos" );
		}
		b.getNev();
		return new ResponseEntity<Beteg>(b, HttpStatus.OK);
    }

	@GetMapping("/{taj}/esetek")
    public ResponseEntity<List<Eset>> listEsetek(@PathVariable("taj") Integer taj, Authentication auth) { 
		return new ResponseEntity<List<Eset>>(esetRepo.findEsetekByBeteg(betegRepo.getOne(taj)),HttpStatus.OK);
	} 

	@GetMapping("/{taj}/{esid}/kezelesek")
    public ResponseEntity<List<Kezeles>> listKezelesek(@PathVariable("taj") Integer taj, @PathVariable("esid") Integer esid, Authentication auth) { 
		return new ResponseEntity<List<Kezeles>>(kezelesRepo.findByEset(esetRepo.getOne(esid)),HttpStatus.OK);
	} 
    		
	@GetMapping("/kezeles/{kid}")
    public ResponseEntity<Kezeles> getKezeles(@PathVariable("kid") Integer kid, @PathVariable("esid") Integer esit, Authentication auth) { return null; } 

	@GetMapping("/kezeles/{kid}/alt_dates")
    public ResponseEntity<Date> getKezelesAltDate(@PathVariable("kid") Integer kid, @PathVariable("esid") Integer esit, Authentication auth) { return null; } 

	@PostMapping("/new")
	public ResponseEntity<Void> createBeteg(@RequestBody Beteg b, UriComponentsBuilder builder) {
                boolean flag = true; 
				betegRepo.save(b);
                if (flag == false) {
        	       return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(builder.path("/{taj}").buildAndExpand(b.getTaj()).toUri());
                return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@PostMapping("/save}")
	public ResponseEntity<Void> saveBeteg(@RequestBody Beteg b) { return null; }

	@PostMapping("/eset/new")
	public ResponseEntity<Eset> newEset(@RequestBody Eset e) {
		log.info("Saving eset...");
		e.setBeteg( betegRepo.getOne( e.getBetegTAJ() ) );
		e.setNyitdate( new Date() );
		e.setStatusz( Eset.Statusz.NYITOTT );
		e = esetRepo.save(e); 
		log.info("Saved eset..." + e.getEsid() + e.getBeteg().getNev());
		return new ResponseEntity<Eset>(e, HttpStatus.CREATED);
	}
	
	@PostMapping("/eset/save")
	public ResponseEntity<Void> saveEset(@RequestBody Eset e) { return null; }
	
	@PostMapping("/kezeles/new")
	public ResponseEntity<Kezeles> saveEset(@RequestBody Kezeles k) {
		k.setEset( esetRepo.getOne( k.getEsetId() ) );
		k.setEllato( ellatoRepo.getOne( k.getEllatoId() ) );

		k.setStatusz( Kezeles.Statusz.ELOJEGYZETT );
		k.setNyitdate( new Date() );
		k=kezelesRepo.save(k);
		return new ResponseEntity<Kezeles>(k, HttpStatus.CREATED);
	}
	
	@PostMapping("/kezeles/orvos")
	public ResponseEntity<Void> setOrvosToEset(@RequestBody Kezeles k ) { return null; }

	@PostMapping("/kezeles/nyit")
	public ResponseEntity<Void> nyitEset(@RequestBody Kezeles k ) { return null; }

	@PostMapping("/kezeles/zar")
	public ResponseEntity<Void> zarEset(@RequestBody Kezeles k ) { return null; }

	@PostMapping("/kezeles/{kid}/change_date")
	public ResponseEntity<Void> setOrvosToEset(@PathVariable("kid") Integer kid, @RequestBody Date d ) { return null; }
}

