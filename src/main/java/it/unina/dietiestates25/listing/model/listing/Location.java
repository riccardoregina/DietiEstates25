package it.unina.dietiestates25.listing.model.listing;

import jakarta.persistence.*;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Location implements Serializable {

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

    @Column(
            name = "address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;

    @Column(
            nullable = false,
            columnDefinition = "geography(Point,4326)"
    )
    private Point coordinates;

    public Location() {}

    public Location(String region, String city, String address, Point coordinates) {
        this.region = region;
        this.city = city;
        this.address = address;
        this.coordinates = coordinates;
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

    public void setCoordinates(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.coordinates = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));
    }

    public double getLongitude() {
        return coordinates.getX();
    }

    public double getLatitude() {
        return coordinates.getY();
    }

    public static Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(address, location.address) &&
                Objects.equals(region, location.region) &&
                Objects.equals(city, location.city) &&
                Objects.equals(coordinates, location.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, region, city, coordinates);
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}

