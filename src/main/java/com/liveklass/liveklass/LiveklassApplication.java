package com.liveklass.liveklass;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class LiveklassApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveklassApplication.class, args);
        log.info("\n\n================================== Started ==================================\n\n");
    }

}
