package com.bbeacon.managers.storage;

import android.content.SharedPreferences;

import com.bbeacon.exceptions.NothingToLoadException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


class StorageLockerTest {

    private SharedPreferences mockSharedPrefs;
    private StorageLocker storageLocker;

    @BeforeEach
    void setUp() {
        mockSharedPrefs = mock(SharedPreferences.class);
        storageLocker = new StorageLocker(mockSharedPrefs);
    }

    @AfterEach
    void tearDown() {
        storageLocker = null;
        mockSharedPrefs = null;
    }

    @Test
    void store_AsBeacon() {
        final String testJson = "testJson.String";

        SharedPreferences.Editor mockSharedPrefsEditor = mock(SharedPreferences.Editor.class);

        when(mockSharedPrefs.edit()).thenReturn(mockSharedPrefsEditor);
        when(mockSharedPrefsEditor.putString(anyString(), anyString())).then(invocation -> {

            Assert.assertEquals(testJson, invocation.getArgument(1));
            return mockSharedPrefsEditor;
        });

        storageLocker.store(testJson, StorageLockerType.StorageKey.BEACON);

        verify(mockSharedPrefs, times(1)).edit();
        verify(mockSharedPrefsEditor, times(1)).putString("beacon", testJson);
        verify(mockSharedPrefsEditor, times(1)).apply();
        verifyNoMoreInteractions(mockSharedPrefsEditor);
        verifyNoMoreInteractions(mockSharedPrefs);
    }

    @Test
    void store_AsRoom() {
        final String testJson = "testJson.String";

        SharedPreferences.Editor mockSharedPrefsEditor = mock(SharedPreferences.Editor.class);

        when(mockSharedPrefs.edit()).thenReturn(mockSharedPrefsEditor);
        when(mockSharedPrefsEditor.putString(anyString(), anyString())).then(invocation -> {

            Assert.assertEquals(testJson, invocation.getArgument(1));
            return mockSharedPrefsEditor;
        });

        storageLocker.store(testJson, StorageLockerType.StorageKey.Single_ROOM);

        verify(mockSharedPrefs, times(1)).edit();
        verify(mockSharedPrefsEditor, times(1)).putString("room", testJson);
        verify(mockSharedPrefsEditor, times(1)).apply();
        verifyNoMoreInteractions(mockSharedPrefsEditor);
        verifyNoMoreInteractions(mockSharedPrefs);
    }

    @Test
    void load_Beacon() throws NothingToLoadException {
        final String testJson = "testJson.String";

        when(mockSharedPrefs.getString("beacon", null)).thenReturn(testJson);

        String resultString = storageLocker.load(StorageLockerType.StorageKey.BEACON);

        Assert.assertEquals(resultString, testJson);
        verify(mockSharedPrefs, times(1)).getString("beacon", null);
    }

    @Test
    void load_Room() throws NothingToLoadException {
        final String testJson = "testJson.String";

        when(mockSharedPrefs.getString("room", null)).thenReturn(testJson);

        String resultString = storageLocker.load(StorageLockerType.StorageKey.Single_ROOM);

        Assert.assertEquals(resultString, testJson);
        verify(mockSharedPrefs, times(1)).getString("room", null);
    }

    @Test
    void load_Throw_NothingToLoadException() throws NothingToLoadException {
        final String testJson = "testJson.String";

        when(mockSharedPrefs.getString("beacon", null)).thenReturn(null);

        Assertions.assertThrows(NothingToLoadException.class, () -> {
            storageLocker.load(StorageLockerType.StorageKey.BEACON);
        });

        verify(mockSharedPrefs, times(1)).getString("beacon", null);
    }
}