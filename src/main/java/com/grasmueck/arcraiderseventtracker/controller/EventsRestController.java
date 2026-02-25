package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.service.FilterEvents;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsRestController {

    private final FilterEvents filterEvents;
    private final MetaforgeClient metaforgeClient;

    public EventsRestController(MetaforgeClient metaforgeClient, FilterEvents filterEvents) {
        this.metaforgeClient = metaforgeClient;
        this.filterEvents = filterEvents;
    }

    @GetMapping("/all")
    public List<MetaforgeEventDto> getAllEvents() {
        return metaforgeClient.collectAllEvents().data();
    }

    @GetMapping("/filtered")
    public List<MetaforgeEventDto> getFilteredEvents(@RequestParam(required = true) String[] args) {
        System.out.println("test");
        System.out.println(args[1].toString());
        Object data = metaforgeClient.collectAllEvents().data();
        if (data != null) {
            System.out.println("API call successful.");

            if (args.length > 0) {
                System.out.println("Filtering events for event name: " + args[0] + " and map name: " + args[1]);
                System.out.println(filterEvents.filterEvents(args[0], args[1], data.toString()));
            } if (args.length > 2) {
                System.out.println("Filtering events for event name: " + args[2] + " and map name: " + args[3]);
                System.out.println(filterEvents.filterEvents(args[2], args[3], data.toString()));
            } if (args.length > 4) {
                System.out.println("Filtering events for event name: " + args[4] + " and map name: " + args[5]);
                System.out.println(filterEvents.filterEvents(args[4], args[5], data.toString()));
            } if (args.length > 6) {
                System.out.println("Filtering events for event name: " + args[6] + " and map name: " + args[7]);
                System.out.println(filterEvents.filterEvents(args[6], args[7], data.toString()));
            }
        }
        return null;
    }
}