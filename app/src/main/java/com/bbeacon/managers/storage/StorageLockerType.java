package com.bbeacon.managers.storage;

import com.bbeacon.exceptions.NothingToLoadException;

public interface StorageLockerType {

    void store(String jsonString, StorageKey storageKey);

    String load(StorageKey storageKey) throws NothingToLoadException;


    enum StorageKey {

        BEACON("beacon"),
        Single_ROOM("room");

        public String getValue() {
            return value;
        }

        private String value;

        StorageKey(String value) {
            this.value = value;
        }
    }
}
