package com.bombanya.kursach.posPredict;

import com.github.amsacode.predict4java.SatPos;
import com.github.amsacode.predict4java.Satellite;
import com.github.amsacode.predict4java.SatelliteFactory;
import com.github.amsacode.predict4java.TLE;

import java.time.Instant;
import java.util.Date;

public class PosPredictUtils {

    public static GeoCoords calcSatPosition(String[] tle, Instant time){
        Satellite sat = SatelliteFactory.createSatellite(new TLE(tle));
        sat.calculateSatelliteVectors(Date.from(time));
        SatPos pos = sat.calculateSatelliteGroundTrack();
        return new GeoCoords(pos.getAltitude(), pos.getLatitude(), pos.getLongitude());
    }

    public static double calcDistanceBetweenSats(GeoCoords a, GeoCoords b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)
                + Math.pow(a.getZ() - b.getZ(), 2));
    }
}
