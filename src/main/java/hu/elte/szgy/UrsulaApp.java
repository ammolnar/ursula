package hu.elte.szgy;
import hu.elte.szgy.rest.BetegManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


@RestController
@SpringBootApplication
public class UrsulaApp {
	private static Logger log = LoggerFactory.getLogger(UrsulaApp.class);

	public static void main(String[] args) throws Exception {
		log.error( "Error log XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );
		log.info( "Info log XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );
		log.debug( "Debug log XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );
		SpringApplication.run(UrsulaApp.class, args);
	}

}
