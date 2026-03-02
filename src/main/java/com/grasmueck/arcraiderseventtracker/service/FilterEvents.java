package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


// Service class to filter events based on requested event name and map name, and extract their start times as hours of the day.
@Service
@Slf4j
public class FilterEvents {

    public ArrayList<Integer> filterEventsByStartTimes(String requestedEventName, String requestedMapName, List<MetaforgeEventDto> collectedData) {
        if (requestedEventName == null || requestedMapName == null || collectedData == null) return new ArrayList<>();

        return collectedData.stream()
                .filter(e -> e.name().contains(requestedEventName) && e.map().contains(requestedMapName))
                .map(e -> getHour(e.startTime()))
                .filter(h -> h != Integer.MAX_VALUE)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Helper: returns hour-of-day (0-23) or Integer.MAX_VALUE when unparsable/null
    private static int getHour(Object t) {
        if (t == null) return Integer.MAX_VALUE;
        if (t instanceof Number) {
            return Instant.ofEpochMilli(((Number) t).longValue()).atZone(ZoneId.systemDefault()).getHour();
        }
        String s = t.toString().trim();
        if (s.contains(":")) {
            return LocalTime.parse(s).getHour();
        }
        try {
            return Instant.ofEpochMilli(Long.parseLong(s)).atZone(ZoneId.systemDefault()).getHour();
        } catch (NumberFormatException e) {
            log.warn("Could not parse time '{}' while extracting hour", s);
            return Integer.MAX_VALUE;
        }
    }
}
