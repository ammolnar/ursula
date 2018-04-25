package hu.elte.szgy.rest;

import hu.elte.szgy.data.User;
import hu.elte.szgy.data.User.UserType;
import hu.elte.szgy.data.UserRepository;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Transactional
public class UserManager {
	private static Logger log = LoggerFactory.getLogger(UserManager.class);

	@Autowired
	private UserRepository userDao;

    private String printUser(User u) {
		return "{ \"username\":\"" +u.getUsername() + "\", " + 
				  "\"type\":\"" +u.getType().name() + "\", " +
				  "\"id\":\"" +u.getUserid() + "\"}";
    }

    @GetMapping("/self")
    public String selfUser(Authentication a) {
		User u = userDao.getOne(a.getName());
		return printUser(u); 
	}
		
	@GetMapping("/{userid}")
    public String otherUser(@PathVariable("userid") String username, Authentication a) {
		User u = userDao.getOne(username);
		if(u.getType() != UserType.BETEG && a.getAuthorities().contains("ROLE_RECEPCIOS")) {
			throw new AccessDeniedException("No access to User: "+username);
		}
		return printUser(u);
	}
		

    @PostMapping("/password")
	public ResponseEntity<Void> setSelfpassword(@RequestBody PassDTO pass, Authentication a) {
    	    return setPassword(a.getName(), pass, a);
    }

    @PostMapping("/password/{userId}")
	public ResponseEntity<Void> setPassword(@PathVariable("userid") String username,
			@RequestBody PassDTO pass, Authentication a) {
    		User u = userDao.getOne(username);
    		if(!username.equals( a.getName() ) &&
    			!a.getAuthorities().contains( "ROLE_ADMIN") &&
    			!(a.getAuthorities().contains( "ROLE_RECEPCIOS") && u.getType() == UserType.BETEG)) {
            	throw new AccessDeniedException("Invalid access to password");
            }
    		u.setPassword( "{noop}"+ pass.getNew_pass() );
    		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/new")
	//public ResponseEntity<Void> createUser(@RequestBody(required=false) User u, UriComponentsBuilder builder) {
	public ResponseEntity<Void> createUser(@RequestBody(required=false) User u, Authentication a) {
    			
    			boolean admin = a.getAuthorities().contains(new SimpleGrantedAuthority( "ROLE_ADMIN"));
    			log.info( "CREATING NEW USER BY", admin ? "ADMIN":"RECEPCIO" );
                if(u.getType() != UserType.BETEG && !admin) {
                	throw new AccessDeniedException("Only authorized to create BETEG users");
                }
                if(u.getType() == UserType.BETEG && admin || u.getType() == UserType.ADMIN) {
                	throw new AccessDeniedException("Only authorized to create REC, LAB & ORVOS users");
                }
                if(!u.getPassword().startsWith( "{" )) u.setPassword( "{noop}"+u.getPassword() );
                if(userDao.existsById( u.getUsername() )) {
                	throw new EntityExistsException("Name already used");
                }
				userDao.save(u);
                log.info( "Creating user: " + u.getUserid() );
                return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
    @PostMapping("/delete/{userid}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userid") String username, Authentication a) {
		User u = userDao.getOne(username);
		if(!a.getAuthorities().contains( "ROLE_ADMIN") &&
    	   !(a.getAuthorities().contains( "ROLE_RECEPCIOS") && u.getType() == UserType.BETEG)) {
            	throw new AccessDeniedException("Not authorized to delete");
        }
		userDao.delete( u );
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/dispatch")
	public ResponseEntity<Void> dispatchUser() {
    	//log.debug("Into URI: " + rr.getURI().toString() );
    	SecurityContext cc = SecurityContextHolder.getContext();
        HttpHeaders headers = new HttpHeaders();
    	if(cc.getAuthentication() != null) {
    		Authentication a=cc.getAuthentication();
            try
    		{
    			headers.setLocation(new URI("/"+userDao.getOne(a.getName()).getType().toString().toLowerCase()+"_home.html"));
    		}
    		catch ( URISyntaxException e )	{ log.warn( "Dispatcher cannot redirect" ); }
    	}
    	
    	return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
    }
    
}

