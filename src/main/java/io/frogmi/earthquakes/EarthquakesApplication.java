package io.frogmi.earthquakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class EarthquakesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EarthquakesApplication.class, args);
    }

}
