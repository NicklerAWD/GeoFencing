package com.nicklerinc.geofenceservice;

/**
 * Created by nicolas on 03/12/14.
 */
public class GeoFencePoint {

    private float point_lat, point_longi, point_radius;

    public GeoFencePoint(float lat, float longi, int radius) {
        point_lat = lat;
        point_longi = longi;
        point_radius = radius;
    }

    public float[] GetPoint() {
        return new float[]{point_lat, point_longi, point_radius};
    }
    public float GetLatitude() {
        return point_lat;
    }
    public float GetLongitude() {
        return point_longi;
    }
    public float GetRadius() {
        return point_radius;
    }
}
