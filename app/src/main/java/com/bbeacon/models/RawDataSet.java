package com.bbeacon.models;

import java.io.Serializable;

public class RawDataSet<T> implements Serializable {

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
