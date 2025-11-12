package com.example.Observer.model;

public interface Observer {
    void update(String message);
    String getObserverType();
}
