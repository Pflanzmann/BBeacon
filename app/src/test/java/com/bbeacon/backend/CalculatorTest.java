package com.bbeacon.backend;

import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @AfterEach
    void tearDown() {
        calculator = null;
    }

    @Test
    void getCoordinate() {
        PositionedBeacon beacon1 = mock(PositionedBeacon.class);
        PositionedBeacon beacon2 = mock(PositionedBeacon.class);
        PositionedBeacon beacon3 = mock(PositionedBeacon.class);
        PositionedBeacon beacon4 = mock(PositionedBeacon.class);

        when(beacon1.getX()).thenReturn(0f);
        when(beacon1.getY()).thenReturn(0f);

        when(beacon2.getX()).thenReturn(4f);
        when(beacon2.getY()).thenReturn(0f);

        when(beacon3.getX()).thenReturn(0f);
        when(beacon3.getY()).thenReturn(4f);

        when(beacon4.getX()).thenReturn(4f);
        when(beacon4.getY()).thenReturn(4f);

        RangedBeaconPosition ranged1 = mock(RangedBeaconPosition.class);
        RangedBeaconPosition ranged2 = mock(RangedBeaconPosition.class);
        RangedBeaconPosition ranged3 = mock(RangedBeaconPosition.class);
        RangedBeaconPosition ranged4 = mock(RangedBeaconPosition.class);

        when(ranged1.getPositionedBeacon()).thenReturn(beacon1);
        when(ranged2.getPositionedBeacon()).thenReturn(beacon2);
        when(ranged3.getPositionedBeacon()).thenReturn(beacon3);
        when(ranged4.getPositionedBeacon()).thenReturn(beacon4);

        when(ranged1.getRange()).thenReturn(2.5d);
        when(ranged1.getRange()).thenReturn(2.5d);
        when(ranged1.getRange()).thenReturn(2.5d);
        when(ranged1.getRange()).thenReturn(2.5d);

        List<RangedBeaconPosition> beaconList = new ArrayList<>();
        beaconList.add(ranged1);
        beaconList.add(ranged2);
        beaconList.add(ranged3);
//        beaconList.add(ranged4);

        UserPosition position = calculator.getCoordinate(beaconList);

        System.out.println("X: " + position.getX() + " |Y: " + position.getY());
    }
}