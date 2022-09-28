package com.epam.javaprogram.springtesting.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Booking {

    private String id;
    private String name;
    private String country;
    private String city;
    private double price;

    @JsonCreator
    public Booking(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("country") String country, @JsonProperty("city") String city, @JsonProperty("price") double price) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public double getPrice() {
        return price;
    }
}
