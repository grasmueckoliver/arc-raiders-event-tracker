package com.grasmueck.arcraiderseventtracker;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import com.grasmueck.arcraiderseventtracker.service.FilterEvents;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@SpringBootApplication
public class ArcRaidersEventTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcRaidersEventTrackerApplication.class, args);
    }
}
