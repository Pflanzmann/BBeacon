package com.bbeacon.models;

public abstract class TaskSuccessfulCallback {

    public void onTaskFinished() {
        throw new RuntimeException("Stub!");
    }

    public void onTaskFailed(String message, Exception e) {
        throw new RuntimeException("Stub!");
    }
}
