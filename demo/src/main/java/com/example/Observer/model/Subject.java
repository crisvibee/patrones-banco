package com.example.Observer.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
        System.out.println(observer.getObserverType() + " registrado para notificaciones");
    }

    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println(observer.getObserverType() + " removido de notificaciones");
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}
