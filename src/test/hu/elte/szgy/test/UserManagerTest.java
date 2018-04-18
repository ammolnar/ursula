package hu.elte.szgy.test;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import hu.elte.szgy.rest.UserManager;

@RunWith(SpringRunner.class)
@WebMvcTest(value=UserManager.class, secure=false)
public class UserManagerTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserManager userManager;

	@Test
	public void greetingShouldReturnMessageFromService() throws Exception
	{
		when(userManager.findUser("Jozsi") ).thenReturn("{ \"username\":\"Jozsi\"" + 
				  "\"type\":\"admin\"" +
				  "\"id\":\"0\"}");

		this.mockMvc.perform(get("/user/Jozsika")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("\"type\":\"admin\"")));
	}
}
