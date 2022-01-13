package com.bombanya.kursach.readable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@ToString
public class ReadableSatData {

    @Id
    private final Integer noradId;
    private final String intldes;
    private final String satName;
    private final String companyName;
    private final LocalDate launch;
    private final LocalDate decay;
    private final String opStatus;
    private final Double mass;
    private final Double volume;
    private final String satelliteType;
    private final String planetName;
    private final Double apogee;
    private final Double perigee;
    private final Double period;
    private final String orbitType;
    private final Double propellantMass;
}
