package hu.elte.szgy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import hu.elte.szgy.rest.UrsulaUserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest
{
	@Autowired
	private UrsulaUserService userService;

	@Test
	public void contextLoads() throws Exception
	{
		assertThat(userService).isNotNull();
		System.out.println( "OK, userService is available" );
	}

	@Test
	public void loadNonExistentUser() throws Exception
	{
		assertThatThrownBy( () -> {userService.loadUserByUsername("Béla");} ) //
			.isInstanceOf( UsernameNotFoundException.class ) //
			.hasMessage("Béla");

		System.out.println( "OK, user Béla does not exist" );
	}

}