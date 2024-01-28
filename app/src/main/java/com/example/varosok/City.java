package com.example.varosok;

public class City {
    private int id;
    private String nev;
    private String orszag;
    private int lakossag;

    // Constructor
    public City(int id, String nev, String orszag, int lakossag) {
        this.id = id;
        this.nev = nev;
        this.orszag = orszag;
        this.lakossag = lakossag;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return nev;
    }

    public String getOrszag() {
        return orszag;
    }

    public int getPopulation() {
        return lakossag;
    }
    public String toString() {
        return "City{id=" + id + ", name='" + nev + "', country='" + orszag + "', population=" + lakossag + "}";
    }
}
