package hu.elte.szgy.rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import hu.elte.szgy.data.User;
import hu.elte.szgy.data.UserDao;

public class UrsulaUserService  implements UserDetailsService {
	private static Logger log = LoggerFactory.getLogger(UrsulaUserService.class);
 
    @Autowired
    private UserDao userRepository;
 
   // @Override
    public UserDetails loadUserByUsername(String username) {
    	log.error("Haho!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	log.debug("HahoDEB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UrsulaUserPrincipal(user);
    }
}
