package hu.elte.szgy.rest;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.elte.szgy.data.User;
import hu.elte.szgy.data.User.UserType;


public class UrsulaUserPrincipal implements UserDetails {
    private User user;
    private List<GrantedAuthority> auths=new ArrayList<GrantedAuthority>(5);
 
    public UrsulaUserPrincipal(User user) {
        this.user = user;
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));

        auths.add(new SimpleGrantedAuthority("ROLE_" + user.getType().name()));
        
        if(user.getType() == UserType.LABOR || user.getType() == UserType.ORVOS) {
        	auths.add(new SimpleGrantedAuthority("ROLE_ELLATO"));
        }
    }

	public java.util.Collection<? extends GrantedAuthority> getAuthorities() { return auths; }  
	public java.lang.String getUsername() { return user.getUsername(); }
	public java.lang.String getPassword() { return user.getPassword(); }

	public boolean isEnabled() { return true; }
	public boolean isCredentialsNonExpired() { return true; }
	public boolean isAccountNonExpired() { return true; }
	public boolean isAccountNonLocked() { return true; }
}
