package com.bbeacon.managers.storage;

import android.content.SharedPreferences;

import com.bbeacon.exceptions.NothingToLoadException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StorageLocker {

    private SharedPreferences sharedPreferences;

    public enum StorageKey {

        BEACON("beacon"),
        Single_ROOM("room");

        private String value;

        StorageKey(String value) {
            this.value = value;
        }
    }

    @Inject
    public StorageLocker(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void store(String jsonString, StorageKey storageKey) {
        SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();

        sharedPrefsEditor.putString(storageKey.value, jsonString);

        sharedPrefsEditor.apply();
    }

    public String load(StorageKey storageKey) throws NothingToLoadException {
        SharedPreferences sharedPrefs = sharedPreferences;

        String returnValue = sharedPrefs.getString(storageKey.value, null);

        if (returnValue == null)
            throw new NothingToLoadException();
        else
            return returnValue;
    }
}
