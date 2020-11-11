package projects.rlstop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RlstopApplication {

    public static void main(String[] args) {
        SpringApplication.run(RlstopApplication.class, args);
    }
}
