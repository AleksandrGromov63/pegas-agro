package com.example.pegasagro.utils;

public class Haversine {
    public static final double equatorialEarthRadius = 6378.1370D;
    public static final double d2r = (Math.PI / 180D);

    public static double calculateDistanceInKM(double lat1, double long1, double lat2, double long2) {

        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));

        return equatorialEarthRadius * c;
    }
}
