package com.bbeacon.models;

public class Beacon {
    private String name;
    private String macAddress;

    public Beacon(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
