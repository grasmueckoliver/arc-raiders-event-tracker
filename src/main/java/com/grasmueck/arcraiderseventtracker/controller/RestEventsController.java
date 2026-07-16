package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.dto.FilteredEventsDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.service.FilterEventsService;
import com.grasmueck.arcraiderseventtracker.service.MetaforgeCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for event management providing endpoints to retrieve all and filtered events from PostgreSQL.
 */
@RestController
@RequestMapping("/events")
@Slf4j
public class RestEventsController {

    private final FilterEventsService filterEventsService;
    private final MetaforgeCacheService cacheService;

    public RestEventsController(MetaforgeCacheService cacheService, FilterEventsService filterEventsService) {
        this.cacheService = cacheService;
        this.filterEventsService = filterEventsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MetaforgeEventDto>> getAllEvents() {
        return ResponseEntity.ok(cacheService.getAllEventsAsDto());
    }

    /**
     * Filters events by event name and map name.
     *
     * @param args array of pairs [eventName1, mapName1, eventName2, mapName2, ...]
     * @return list of filtered events with their start times
     */
    @GetMapping("/filtered")
    public ResponseEntity<List<FilteredEventsDto>> getFilteredEvents(@RequestParam String[] args) {
        if (args == null || args.length < 2) {
            log.warn("Invalid filter parameters: args length must be >= 2, got {}", args != null ? args.length : 0);
            return ResponseEntity.badRequest().build();
        }

        if (args.length % 2 != 0) {
            log.warn("Invalid filter parameters: args length must be even, got {}", args.length);
            return ResponseEntity.badRequest().build();
        }

        List<MetaforgeEventDto> allEvents = cacheService.getAllEventsAsDto();
        List<FilteredEventsDto> filteredEvents = new ArrayList<>();

        for (int i = 0; i < args.length; i += 2) {
            String eventName = args[i];
            String mapName = args[i + 1];

            log.info("Filtering events for event name: '{}' and map name: '{}'", eventName, mapName);

            List<Integer> times = filterEventsService.filterEventsByStartTimes(eventName, mapName, allEvents);
            filteredEvents.add(new FilteredEventsDto(eventName, mapName, times));
        }

        return ResponseEntity.ok(filteredEvents);
    }
}