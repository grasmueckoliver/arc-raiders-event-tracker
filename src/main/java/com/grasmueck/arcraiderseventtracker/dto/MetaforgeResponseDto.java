package com.grasmueck.arcraiderseventtracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MetaforgeResponseDto(
        List<MetaforgeEventDto> data,
        long cachedAt
) {}

