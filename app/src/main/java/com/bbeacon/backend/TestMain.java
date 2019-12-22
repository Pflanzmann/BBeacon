package com.bbeacon.backend;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import com.bbeacon.models.TaskSuccessfulCallback;

import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("Start Main");

        String test = "testDefault";

        Observable testObservable =  Observable.create(emitter -> {
            TaskSuccessfulCallback callback = new TaskSuccessfulCallback() {
                @Override
                public void onTaskFinished() {
                    emitter.onNext("1");
                }
            };

            callback.onTaskFinished();
            callback.onTaskFinished();

        });


        System.out.println("ende Main");

        System.out.println("ende Programm");
    }
}
