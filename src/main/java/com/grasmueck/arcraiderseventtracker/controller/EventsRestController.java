package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.client.CollectMetaforgeApi;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/events")
public class EventsRestController {

    @RequestMapping("/all")
    public JSONObject getAllEvents() {
        CollectMetaforgeApi collectEventDataClient = new CollectMetaforgeApi();
            return new JSONObject(collectEventDataClient.collectAllEvents());
    }
}
