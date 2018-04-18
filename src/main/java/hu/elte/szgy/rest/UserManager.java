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
import hu.elte.szgy.data.User;
import hu.elte.szgy.data.UserDao;

@RestController
@RequestMapping("user")
public class UserManager {
	private static Logger log = LoggerFactory.getLogger(UserManager.class);

	@Autowired
	private UserDao userDao;

	@GetMapping("/{userid}")
    public String findUser(@PathVariable("userid") String username) {
		User u = userDao.findByUsername(username);
	
		return "{ \"username\":\"" +u.getUsername() + "\"" + 
				  "\"type\":\"" +u.getType().name() + "\"" +
				  "\"id\":\"" +u.getUserid() + "\"}";
    }

    @PostMapping("/new")
	public ResponseEntity<Void> createUser(@RequestBody(required=false) User u, UriComponentsBuilder builder) {
    	log.error( "CREATING NEW USER" );
                boolean flag = true; 
				userDao.addUser(u);
                if (flag == false) {
        	       return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                }
                log.info( "Creating user: " + u.getUserid() );
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(builder.path("/{userid}").buildAndExpand(u.getUserid()).toUri());
                return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}

