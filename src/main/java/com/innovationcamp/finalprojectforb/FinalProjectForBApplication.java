package com.innovationcamp.finalprojectforb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinalProjectForBApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectForBApplication.class, args);
    }

}
