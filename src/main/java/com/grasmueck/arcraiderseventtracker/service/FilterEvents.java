package com.grasmueck.arcraiderseventtracker.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FilterEvents {


    public ArrayList<Integer> filterEvents(String requestedEventName, String requestedMapName, String collectedData) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse((collectedData));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            ArrayList<JSONObject> filteredEvents = new ArrayList<>();
            ArrayList<Integer> timeArray = new ArrayList<>();

            for (Object event : jsonArray) {

                JSONObject eventObj = (JSONObject) event;

                if (requestedEventName != null && requestedMapName != null) {
                    if (eventObj.get("name").equals(requestedEventName) &&
                            eventObj.get("map").equals(requestedMapName)) {

                        Time time = new Time((long) eventObj.get("startTime"));
                        JSONObject filterObj = new JSONObject(
                                Map.of("name", eventObj.get("name"),
                                        "map", eventObj.get("map"),
                                        "time", time));

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
        } catch (ParseException e) {
            log.error("Failed to parse JSON: {}", e.getMessage());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
        }
        return null;
    }
}

