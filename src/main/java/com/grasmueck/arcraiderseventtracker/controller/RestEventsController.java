package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.dto.FilteredEventsDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventRepository;
import com.grasmueck.arcraiderseventtracker.service.FilterEventsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This controller class defines REST endpoints for retrieving all events and filtered events based on event name and map name.
 * It uses the MetaforgeClient to fetch event data and the FilterEvents service to filter the events according to the specified criteria.
 **/
@RestController
@RequestMapping("/events")
public class RestEventsController {

    private final FilterEventsService filterEventsService;
    private final MetaforgeEventRepository repository;

    public RestEventsController(MetaforgeEventRepository repository, FilterEventsService filterEventsService) {
        this.repository = repository;
        this.filterEventsService = filterEventsService;
    }


    @GetMapping("/all")
    public List<MetaforgeEventDto> getAllEvents() {
        return repository.findAll().stream()
                .map(e -> new MetaforgeEventDto(
                        e.getName(),
                        e.getIcon(),
                        e.getStartTime() != null ? e.getStartTime().toEpochMilli() : 0L,
                        e.getEndTime() != null ? e.getEndTime().toEpochMilli() : 0L,
                        e.getMapName()))
                .collect(Collectors.toList());
    }

    /**
     * @param args an array of strings where each pair of elements represents an event name and a map name to filter by.
     *             For example, args[0] is the first event name, args[1] is the first map name,
     *             args[2] is the second event name, args[3] is the second map name, and so on.
     * @return a list of FilteredEventsDto, each containing the event name, map name
     * and a list of start times (hours of the day) for events matching the criteria.
     */
    @GetMapping("/filtered")
    public List<FilteredEventsDto> getFilteredEvents(@RequestParam String[] args) {

        List<MetaforgeEventDto> data = getAllEvents();
        List<FilteredEventsDto> filteredEvents = new ArrayList<>();

        if (data != null && args != null && args.length >= 2) {
            System.out.println("Filtering events for event name: " + args[0] + " and map name: " + args[1]);
            filteredEvents.add(
                    new FilteredEventsDto(
                            args[0],
                            args[1],
                            filterEventsService.filterEventsByStartTimes(args[0], args[1], data)));

            if (args.length > 2) {
                System.out.println("Filtering events for event name: " + args[2] + " and map name: " + args[3]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[2],
                                args[3],
                                filterEventsService.filterEventsByStartTimes(args[2], args[3], data)));

            }
            if (args.length > 4) {
                System.out.println("Filtering events for event name: " + args[4] + " and map name: " + args[5]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[4],
                                args[5],
                                filterEventsService.filterEventsByStartTimes(args[4], args[5], data)));

            }
            if (args.length > 6) {
                System.out.println("Filtering events for event name: " + args[6] + " and map name: " + args[7]);
                filteredEvents.add(
                        new FilteredEventsDto(
                                args[6],
                                args[7],
                                filterEventsService.filterEventsByStartTimes(args[6], args[7], data)));
            }
        }
        return filteredEvents;
    }
}