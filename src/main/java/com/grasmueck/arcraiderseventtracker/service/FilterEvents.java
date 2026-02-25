package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilterEvents {

    private final MetaforgeClient metaforgeClient;

    public FilterEvents(MetaforgeClient metaforgeClient) {
        this.metaforgeClient = metaforgeClient;
    }

    public ArrayList<Integer> filterEvents(String requestedEventName, String requestedMapName, String collectedData) {
        try {

            ArrayList<JSONObject> filteredEvents = new ArrayList<>();
            ArrayList<Integer> timeArray = new ArrayList<>();

            for (MetaforgeEventDto event : metaforgeClient.collectAllEvents().data()) {
                if (requestedEventName != null && requestedMapName != null) {
                    if (event.name().equals(requestedEventName) &&
                            event.map().equals(requestedMapName)) {

                        Time time = new Time((long) event.startTime());
                        JSONObject filterObj = new JSONObject(
                                Map.of("name", event.name(),
                                        "map", event.map(),
                                        "time", event.startTime()));

                        filteredEvents.add(filterObj);
                    }
                }
            }

            for (JSONObject filteredEvent : filteredEvents) {
                timeArray.add(Integer.parseInt(filteredEvent.get("time").toString().split(":")[0]));
            }

            timeArray = timeArray.stream().sorted().collect(Collectors.toCollection(ArrayList::new));

            return timeArray;
        } catch (NullPointerException e) {
            log.error("Null value encountered: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
        }
        return null;
    }
}

