package com.grasmueck.arcraiderseventtracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;


@EnableScheduling
@RestController
@Slf4j
@SpringBootApplication
public class ArcRaidersEventTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcRaidersEventTrackerApplication.class, args);
    }
}
