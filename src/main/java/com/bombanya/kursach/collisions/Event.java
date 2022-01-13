package com.bombanya.kursach.collisions;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
public class Event {

    @Id
    private long eventId;
    @Column("norad_id_1")
    private Integer noradId1;
    @Column("norad_id_2")
    private Integer noradId2;
    private Instant predictedEventTime;
    private Instant eventCreationTime;
    private Integer maneuveringSat;
    private Double distance;
}
