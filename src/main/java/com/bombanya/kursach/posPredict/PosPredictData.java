package com.bombanya.kursach.posPredict;

import lombok.*;
import org.springframework.data.annotation.Id;

@RequiredArgsConstructor
@Getter
@ToString
public class PosPredictData {

    @Id
    private final int noradId;
    private final double planetMass;
    private final double planetRadius;
    private final double inclination;
    private final double longitudeOfTheAscendingNode;
    private final double eccentricity;
    private final double argumentOfPeriapsis;
    private final double meanMotion;
    private final double meanAnomaly;
    private final String tleEpoch;
    private final String bstar;

    public String[] tle(){
        String[] result = new String[3];
        result[0] = "Sat";
        result[1] = "1 000000 000000   " + tleEpoch + "  .00000000  00000-0 " + String.format("%8s", bstar) + " 0  0000";
        result[2] = "2 00000 " + String.format("%8.4f %8.4f ", inclination, longitudeOfTheAscendingNode) +
                String.format("%.7f ", eccentricity).substring(2)
                + String.format("%8.4f %8.4f %11f000000", argumentOfPeriapsis, meanAnomaly, meanMotion);
        return result;
    }
}
