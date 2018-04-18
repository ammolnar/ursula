package hu.elte.szgy.rest;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import hu.elte.szgy.data.BetegDao;
import hu.elte.szgy.data.Beteg;

@RestController
@RequestMapping("beteg")
public class BetegManager {
	private static Logger log = LoggerFactory.getLogger(BetegManager.class);

	@Autowired
	private BetegDao betegDao;

	@GetMapping("/{taj}")
    public ResponseEntity<Beteg> findBeteg(@PathVariable("taj") Integer taj, 
    		Principal principal, Authentication authentication) {
		if(log.isDebugEnabled()) log.debug("Principal: " + principal.getName() + 
				  " Roles " + authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")) +
		          " RolesN " + authentication.getAuthorities().contains(new SimpleGrantedAuthority("XXXUSER")));
		return new ResponseEntity<Beteg>(betegDao.getBetegByTaj(taj), HttpStatus.OK);
    }

		
    @PostMapping("/")
	public ResponseEntity<Void> createBeteg(@RequestBody Beteg b, UriComponentsBuilder builder) {
                boolean flag = true; 
				betegDao.addBeteg(b);
                if (flag == false) {
        	       return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(builder.path("/{taj}").buildAndExpand(b.getTaj()).toUri());
                return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}

