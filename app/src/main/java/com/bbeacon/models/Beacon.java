package com.bbeacon.models;

public class Beacon {
    private String name;
    private String macAddress;

    private int[] range0;
    private int[] range1;
    private int[] range2;
    private int[] range3;
    private int[] range4;
    private int[] range5;
    private int[] range6;
    private int[] range7;
    private int[] range8;
    private int[] range9;

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

    public int[] getRangeArray(int field) {
        switch (field) {
            case 0:
                return range0;

            case 1:
                return range1;

            case 2:
                return range2;

            case 3:
                return range3;

            case 4:
                return range4;

            case 5:
                return range5;

            case 6:
                return range6;

            case 7:
                return range7;

            case 8:
                return range8;

            case 9:
                return range9;

            default:
                throw new IllegalStateException("Unexpected value: " + field);
        }
    }
}
