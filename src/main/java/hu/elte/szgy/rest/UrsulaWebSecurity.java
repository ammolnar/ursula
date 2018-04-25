package hu.elte.szgy.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class UrsulaWebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/","/extjs/**").permitAll()
                .antMatchers(HttpMethod.GET,"/beteg/self").hasRole("BETEG")
                .antMatchers(HttpMethod.GET,"/beteg/*", "beteg/kezeles/*").hasRole("DOLGOZO")
                .antMatchers(HttpMethod.GET,"/beteg/*/esetek","/beteg/*/*/kezelesek").hasAnyRole("ADMIN","ORVOS","RECEPCIO","BETEG")
                .antMatchers(HttpMethod.GET,"/beteg/kezelesek/*/alt_dates").hasAnyRole("RECEPCIO","BETEG")
                .antMatchers(HttpMethod.GET,"/ellato/**","osztaly/**").hasRole("DOLGOZO")
                .antMatchers(HttpMethod.GET,"/user/self").authenticated()
                .antMatchers(HttpMethod.GET,"/user/*").hasAnyRole("ADMIN","RECEPCIO")
                .antMatchers(HttpMethod.POST,"/beteg/new","/beteg/*","beteg/*/eset/new").hasRole("RECEPCIO")
                .antMatchers(HttpMethod.POST,"/beteg/*/eset/*").hasRole("ORVOS")
                .antMatchers(HttpMethod.POST,"/beteg/eset/<eid>/kezeles/new").hasAnyRole("RECEPCIO","ORVOS")
                .antMatchers(HttpMethod.POST,"/beteg/kezeles/*/set_orvos").hasAnyRole("ORVOS","ADMIN")
                .antMatchers(HttpMethod.POST,"/beteg/kezeles/*/nyit", "beteg/kezeles/*/zar").hasAnyRole("ORVOS","LABOR")
                .antMatchers(HttpMethod.POST,"/beteg/kezeles/*/select_date").hasAnyRole("BETEG","RECEPCIO")
                .antMatchers(HttpMethod.POST,"/ellato/**","osztaly/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/user/new").hasAnyRole("ADMIN","RECEPCIO")
                .antMatchers(HttpMethod.POST,"/user/password").authenticated()
                .antMatchers(HttpMethod.POST,"/user/password/*").hasAnyRole("ADMIN", "RECEPCIO")
                .antMatchers(HttpMethod.POST,"/user/delete/*").hasAnyRole("ADMIN", "RECEPCIO")
                .and()
            .csrf().disable()
            .formLogin()
                .loginPage("/login")
                .successForwardUrl( "/user/dispatch" )
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

//  SIMPLE USERSERVICE TO BE USED FOR TESTING ONLY     
/*     UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);*/ 

		return new UrsulaUserService();
    }
}
