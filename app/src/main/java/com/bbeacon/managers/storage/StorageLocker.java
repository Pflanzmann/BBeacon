package com.bbeacon.managers.storage;

import android.content.SharedPreferences;

import com.bbeacon.exceptions.NothingToLoadException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StorageLocker implements StorageLockerType{

    private SharedPreferences sharedPreferences;

    @Inject
    public StorageLocker(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void store(String jsonString, StorageLockerType.StorageKey storageKey) {
        SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();

        sharedPrefsEditor.putString(storageKey.getValue(), jsonString);

        sharedPrefsEditor.apply();
    }

    @Override
    public String load(StorageLockerType.StorageKey storageKey) throws NothingToLoadException {
        SharedPreferences sharedPrefs = sharedPreferences;

        String returnValue = sharedPrefs.getString(storageKey.getValue(), null);

        if (returnValue == null)
            throw new NothingToLoadException();
        else
            return returnValue;
    }
}
