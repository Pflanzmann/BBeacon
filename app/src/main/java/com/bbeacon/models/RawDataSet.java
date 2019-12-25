package com.bbeacon.models;

public class RawDataSet<T> {

    private int key;
    private T[] set;

    public RawDataSet(int key, T[] set) {
        this.key = key;
        this.set = set;
    }

    public T[] getSet() {
        return set;
    }

    public int getKey() {
        return key;
    }
}
