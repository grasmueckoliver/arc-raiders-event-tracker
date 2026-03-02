package com.grasmueck.arcraiderseventtracker.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record FilteredEventsDto(
    @Getter
    String name,
    @Getter
    String map,
    @Getter
    List<Integer> times

) {}
