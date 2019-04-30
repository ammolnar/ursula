package ursula;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.elte.szgy.UrsulaApp;
import hu.elte.szgy.data.Ellato;
import hu.elte.szgy.data.Osztaly;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrsulaApp.class)
public class EllatoManagerTest {
    private static Logger log = LoggerFactory.getLogger(EllatoManagerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
	log.info("SetUp executing");
	DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
	this.mockMvc = builder.build();
    }

    @After
    public void tearDown() throws Exception {
	log.info("TearDown executing");
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
	    throws JsonParseException, JsonMappingException, IOException {

	ObjectMapper objectMapper = new ObjectMapper();
	return objectMapper.readValue(json, clazz);
    }

    @Test
    public void testGetAllEllato() throws Exception {
	String uri = "/ellato/all";
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
		.andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	Ellato[] ellatolist = mapFromJson(content, Ellato[].class);
	log.info("GetAllEllato returned " + ellatolist.length + " ellato");
	for(Ellato e : ellatolist) {
	    log.info("Item " + e.getType() + " N: " + e.getNev() + " ID: " + e.getElid() + " C:" + e.getClass());
	}
	assertTrue(ellatolist.length > 0);

    }

    @Test
    public void testGetAllOsztaly() throws Exception {
	String uri = "/ellato/osztaly/all";
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
		.andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	Osztaly[] osztalylist = mapFromJson(content, Osztaly[].class);
	log.info("GetAllOsztaly returned " + osztalylist.length + " osztaly");
	for(Osztaly o : osztalylist) {
	    log.info("Item " +  o.getNev() + " V: " + o.getVezetoId() + " V2:" + o.getVezeto());
	}
	assertTrue(osztalylist.length > 0);
    }

    @Test
    public void testGetOsztaly() throws Exception {
	URI uri = new URI("/ellato/osztaly/Szemészet");
	MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
		.andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	log.info("GetOsztaly returned: '" + content + "'");
	Osztaly o = mapFromJson(content, Osztaly.class);
	log.info("GetOsztaly returned " + o.getNev() + " osztaly from: '" + content + "'");
	assertTrue(o.getNev() != null);
    }
}
