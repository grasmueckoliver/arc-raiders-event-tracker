package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.FilteredEventsDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.service.FilterEvents;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


// This controller class defines REST endpoints for retrieving all events and filtered events based on event name and map name.
// It uses the MetaforgeClient to fetch event data and the FilterEvents service to filter the events according to the specified criteria.
@RestController
@RequestMapping("/events")
public class RestEventsController {

    private final FilterEvents filterEvents;
    private final MetaforgeClient metaforgeClient;

    public RestEventsController(MetaforgeClient metaforgeClient, FilterEvents filterEvents) {
        this.metaforgeClient = metaforgeClient;
        this.filterEvents = filterEvents;
    }

    @GetMapping("/all")
    public List<MetaforgeEventDto> getAllEvents() {
        return metaforgeClient.collectAllEvents().data();
    }

    @GetMapping("/filtered")
    public List<FilteredEventsDto> getFilteredEvents(@RequestParam String[] args) {

        List<MetaforgeEventDto> data = metaforgeClient.collectAllEvents().data();
        List<FilteredEventsDto> filteredEvents = new ArrayList<>();

        if (data != null) {
            System.out.println("Filtering events for event name: " + args[0] + " and map name: " + args[1]);
            filteredEvents.add(
                    new FilteredEventsDto(
                            args[0],
                            args[1],
                            filterEvents.filterEventsByStartTimes(args[0], args[1], data)));

            if (args.length > 2) {
                System.out.println("Filtering events for event name: " + args[2] + " and map name: " + args[3]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[0],
                                args[1],
                                filterEvents.filterEventsByStartTimes(args[0], args[1], data)));

            } if (args.length > 4) {
                System.out.println("Filtering events for event name: " + args[4] + " and map name: " + args[5]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[0],
                                args[1],
                                filterEvents.filterEventsByStartTimes(args[0], args[1], data)));

            } if (args.length > 6) {
                System.out.println("Filtering events for event name: " + args[6] + " and map name: " + args[7]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[0],
                                args[1],
                                filterEvents.filterEventsByStartTimes(args[0], args[1], data)));
            }
        }
        return filteredEvents;
    }
}