package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbeacon.models.CalibratedBeacon;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeaconStorageManagerTest {

    private final String PREFS_KEY = "bbeacon";
    private final String BEACONS_KEY = "beacons";
    private final String DEFAULT_VALUE = "";
    private final String JSON_STORAGE_RESULT = "{\"beacons\":{\"deviceId\":{\"calibrationDate\":\"Jan 1, 1970 1:00:00 AM\",\"dataSets\":[],\"deviceName\":\"deviceName\",\"measurementCount\":42,\"calibrationSteps\":42,\"deviceId\":\"deviceId\",\"macAddress\":\"macAddress\"}}}";

    private Context mockContext;
    private BeaconStorageManager beaconStorageManager;

    @BeforeEach
    void setUp() {
        mockContext = mock(Context.class);
        beaconStorageManager = new BeaconStorageManager(mockContext);
    }

    @AfterEach
    void tearDown() {
        mockContext = null;
        beaconStorageManager = null;
    }

    @Test
    void storeBeacon_WithoutPrestorage() {
        SharedPreferences mockPrefs = mock(SharedPreferences.class);
        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);

        CalibratedBeacon beacon1 = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        String beacon1String = new Gson().toJson(beacon1);

        //Get Prefs
        when(mockContext.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)).thenReturn(mockPrefs);
        when(mockPrefs.getString(BEACONS_KEY, DEFAULT_VALUE)).thenReturn(DEFAULT_VALUE);
        when(mockPrefs.edit()).thenReturn(mockEditor);

        when(mockEditor.putString(anyString(), anyString())).then(invocation -> {
            Assert.assertEquals(BEACONS_KEY, invocation.getArgument(0));
            Assert.assertEquals(JSON_STORAGE_RESULT, invocation.getArgument(1));
            return mockEditor;
        });

        beaconStorageManager.storeBeacon(beacon1);

        verify(mockEditor, times(1)).apply();
    }

    @Test
    void loadAllBeacons() {
    }

    @Test
    void loadBeaconById() {
    }

    @Test
    void deletebeaconById() {
    }
}