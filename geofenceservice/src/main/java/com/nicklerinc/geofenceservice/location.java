package com.nicklerinc.geofenceservice;

/**
 * Created by nicolas on 03/12/14.
 */
public class location {
    private double lat;
    private double longi;
    private double accuracy;

    public void setLocation(double lati, double longit, double accur) {
        lat = lati;
        longi = longit;
        accuracy = accur;
    }

    public double[] getLocation() {
        return new double[]{lat, longi, accuracy};
    }
}
