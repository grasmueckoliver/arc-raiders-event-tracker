package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventEntity;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


// Simple caching service that fetches events from Metaforge and upserts them into the relational DB using JPA.
@Service
@Slf4j
@RequiredArgsConstructor
public class MetaforgeCacheService {

    private final MetaforgeClient metaforgeClient;
    private final MetaforgeEventRepository repository;

    @Transactional
    public void refreshCache() {
        MetaforgeResponseDto response = metaforgeClient.collectAllEvents();
        if (response == null || response.data() == null) {
            log.warn("No data returned from MetaforgeClient");
            return;
        }

        List<MetaforgeEventEntity> toSave = new ArrayList<>();
        Instant now = Instant.now();

        for (MetaforgeEventDto dto : response.data()) {
            Instant startInstant = Instant.ofEpochMilli(dto.startTime());
            Instant endInstant = Instant.ofEpochMilli(dto.endTime());

            MetaforgeEventEntity entity = repository.findByNameAndMapNameAndStartTime(dto.name(), dto.map(), startInstant)
                    .orElseGet(() -> MetaforgeEventEntity.builder().name(dto.name()).mapName(dto.map()).startTime(startInstant).build());

            entity.setName(dto.name());
            entity.setIcon(dto.icon());
            entity.setMapName(dto.map());
            entity.setStartTime(startInstant);
            entity.setEndTime(endInstant);
            entity.setLastUpdated(now);

            toSave.add(entity);
        }

        repository.saveAll(toSave);
        log.info("Refreshed {} events into DB", toSave.size());
    }
}
