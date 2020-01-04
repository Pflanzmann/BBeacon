package com.bbeacon.managers;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.models.BleScanResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

@Singleton
public class BleManager implements BleManagerType {

    private BluetoothFinderType bluetoothFinder;

    private PublishSubject<List<BleScanResult>> publisher = PublishSubject.create();

    @Inject
    public BleManager(BluetoothFinderType bluetoothFinder) {
        this.bluetoothFinder = bluetoothFinder;
    }

    public Observable<List<BleScanResult>> getScanningObservable(ArrayList<String> macAddressFilter) throws ScanFilterInvalidException {

        bluetoothFinder.StartFinding(macAddressFilter, new BluetoothCallback() {
            @Override
            public void onNewScanResults(List<BleScanResult> results) {
                publisher.onNext(results);
            }
        });

        return publisher;
    }

    @Override
    public void stopScanning() {
        bluetoothFinder.stopScanning();
    }

    abstract class BluetoothCallback {
        public void onNewScanResults(List<BleScanResult> results) {
        }
    }
}
