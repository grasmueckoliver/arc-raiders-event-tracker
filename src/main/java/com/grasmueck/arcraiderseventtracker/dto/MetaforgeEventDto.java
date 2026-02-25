package com.grasmueck.arcraiderseventtracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


@JsonIgnoreProperties(ignoreUnknown = true)
public record MetaforgeEventDto(
        @Getter
        String name,
        String icon,
        long startTime,
        long endTime,
        @Getter
        String map
) {}