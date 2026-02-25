package com.grasmueck.arcraiderseventtracker;

import com.grasmueck.arcraiderseventtracker.client.CollectMetaforgeApi;
import com.grasmueck.arcraiderseventtracker.service.FilterEvents;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ArcRaidersEventTrackerApplication {
    public static JSONObject collectedData;

    public static void main(String[] args) {
        SpringApplication.run(ArcRaidersEventTrackerApplication.class, args);
        CollectMetaforgeApi collectEventDataClient = new CollectMetaforgeApi();

        collectedData = collectEventDataClient.collectAllEvents();

        if (collectedData != null) {
            System.out.println("API call successful.");
            FilterEvents filterEvents = new FilterEvents();

            if (args.length > 0) {
                System.out.println("Filtering events for event name: " + args[0] + " and map name: " + args[1]);
                System.out.println(filterEvents.filterEvents(args[0], args[1], collectedData.toString()));
            } if (args.length > 2) {
                System.out.println("Filtering events for event name: " + args[2] + " and map name: " + args[3]);
                System.out.println(filterEvents.filterEvents(args[2], args[3], collectedData.toString()));
            } if (args.length > 4) {
                System.out.println("Filtering events for event name: " + args[4] + " and map name: " + args[5]);
                System.out.println(filterEvents.filterEvents(args[4], args[5], collectedData.toString()));
            } if (args.length > 6) {
                System.out.println("Filtering events for event name: " + args[6] + " and map name: " + args[7]);
                System.out.println(filterEvents.filterEvents(args[6], args[7], collectedData.toString()));
            }
        }
    }
}
