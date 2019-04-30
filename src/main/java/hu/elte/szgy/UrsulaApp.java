package hu.elte.szgy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;


@SpringBootApplication
public class UrsulaApp {
        private static Logger log = LoggerFactory.getLogger(UrsulaApp.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(UrsulaApp.class, args);
	}

/*        @Bean
        public Module hibernate5Module()
        {
            log.info("Enabling Hibernate5Module");
            return new Hibernate5Module();
        }
*/
}
