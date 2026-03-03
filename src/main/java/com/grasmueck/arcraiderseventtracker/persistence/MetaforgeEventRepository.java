package com.grasmueck.arcraiderseventtracker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;


public interface MetaforgeEventRepository extends JpaRepository<MetaforgeEventEntity, Long> {
    Optional<MetaforgeEventEntity> findByNameAndMapNameAndStartTime(String name, String mapName, Instant startTime);
}
