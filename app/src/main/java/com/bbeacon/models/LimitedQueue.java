package com.bbeacon.models;

import java.util.ArrayDeque;

public class LimitedQueue<E> extends ArrayDeque<E> {

    private final int max;


    public LimitedQueue(int max) {
        super(max);
        this.max = max;
    }

    @Override
    public boolean add(E o) {
        boolean added = super.add(o);
        while (added && size() > max) {
            super.remove();
        }
        return added;
    }

    @Override
    public void addLast(E e) {
        super.addLast(e);
        while (size() > max) {
            super.removeLast();
        }
    }

    @Override
    public boolean offerLast(E e) {
        boolean added = super.offerLast(e);
        while (added && size() > max) {
            super.pollLast();
        }
        return added;
    }
}
