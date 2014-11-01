package com.example.simon_000.buddy.customs;

/**
 * Created by simon_000 on 2014-10-27.
 */
public class members {

    private String member;
    private double longitude;
    private double latitude;

    public members(String member, double longitude, double latitude){
        this.member = member;
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public members(){

    }

    public String getName() {
        return member;
    }

    public void setName(String member) {
        this.member = member;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
