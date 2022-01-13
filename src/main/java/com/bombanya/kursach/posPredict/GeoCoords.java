package com.bombanya.kursach.posPredict;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GeoCoords {

    private final double altitude;
    private final double latitude;
    private final double longitude;

    private final double x;
    private final double y;
    private final double z;

    public GeoCoords(double altitude, double latitude, double longitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;

        this.x = altitude * Math.cos(latitude) * Math.cos(longitude);
        this.y = altitude * Math.cos(latitude) * Math.sin(longitude);
        this.z = altitude * Math.sin(latitude);
    }
}
