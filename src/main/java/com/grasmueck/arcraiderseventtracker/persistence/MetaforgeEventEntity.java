package com.grasmueck.arcraiderseventtracker.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


//JPA entity to cache Metaforge events relationally.
@Entity
@Table(name = "metaforge_event", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "map_name", "start_time"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaforgeEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "map_name", nullable = false)
    private String mapName;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;
}
