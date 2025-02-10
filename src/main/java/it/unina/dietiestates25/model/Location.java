package it.unina.dietiestates25.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Location implements Serializable {
    @Column(
            name = "address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;
    @Column(
            name = "region",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String region;
    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String city;

    // TODO Replace latitude and longitude columns with GEOGRAPHY
    @Column(
            name = "latitude",
            nullable = false
    )
    private double latitude;
    @Column(
            name = "longitude",
            nullable = false
    )
    private double longitude;
    public Location() {}

    public Location(String address, String region, String city, double latitude, double longitude) {
        this.address = address;
        this.region = region;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(latitude, location.latitude) == 0 && Double.compare(longitude, location.longitude) == 0 && Objects.equals(address, location.address) && Objects.equals(region, location.region) && Objects.equals(city, location.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, region, city, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

