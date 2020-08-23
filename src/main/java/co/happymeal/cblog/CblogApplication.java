package co.happymeal.cblog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author lyd
 */
@SpringBootApplication
public class CblogApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CblogApplication.class);
        builder.headless(false).run(args);
        //SpringApplication.run(CblogApplication.class, args);
    }

}
