package com.grasmueck.arcraiderseventtracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


@JsonIgnoreProperties(ignoreUnknown = true)
public record MetaforgeEventDto(
        @Getter
        String name,
        @Getter
        String icon,
        @Getter
        long startTime,
        @Getter
        long endTime,
        @Getter
        String map
) {}