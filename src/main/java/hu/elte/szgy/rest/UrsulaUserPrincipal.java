package hu.elte.szgy.rest;


import hu.elte.szgy.data.User;
import hu.elte.szgy.data.User.UserType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UrsulaUserPrincipal implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
    private List<GrantedAuthority> auths=new ArrayList<GrantedAuthority>(5);
 
    public UrsulaUserPrincipal(User user) {
        this.user = user;

        // assign role from usertype field: ROLE_BETEG, ROLE_ORVOS, etc
        auths.add(new SimpleGrantedAuthority("ROLE_" + user.getType().name()));
        
        if(user.getType() == UserType.LABOR || user.getType() == UserType.ORVOS) {
        	auths.add(new SimpleGrantedAuthority("ROLE_ELLATO"));
        }
        if(user.getType() != UserType.BETEG) {
        	auths.add(new SimpleGrantedAuthority("ROLE_DOLGOZO"));
        }
    }

	public java.util.Collection<? extends GrantedAuthority> getAuthorities() { return auths; }  
	public java.lang.String getUsername() { return user.getUsername(); }
	public java.lang.String getPassword() { return user.getPassword(); }
	public int getUrsulaId() { return user.getUserid(); }

	public boolean isEnabled() { return true; }
	public boolean isCredentialsNonExpired() { return true; }
	public boolean isAccountNonExpired() { return true; }
	public boolean isAccountNonLocked() { return true; }
}
