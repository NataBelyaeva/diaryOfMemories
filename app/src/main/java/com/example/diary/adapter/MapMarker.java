package com.example.diary.adapter;

import com.yandex.mapkit.geometry.Point;

public class MapMarker {
    private int id;
    private double latitude;
    private double longitude;
    private String title;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Point getPoint() {
        return new Point(latitude, longitude);
    }
}
