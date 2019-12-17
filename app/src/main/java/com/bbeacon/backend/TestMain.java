package com.bbeacon.backend;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestMain {
    public static void main(String[] args) {

        final CompletableFuture<String> testString = new CompletableFuture<>();

        new Thread(){
            @Override
            public synchronized void start() {
                super.start();
                System.out.println("start Thead");

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                testString.complete("Test");
                System.out.println("ende Thead");
            }
        }.start();

        try {
            System.out.println(testString.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ende Main");
    }
}
