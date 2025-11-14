package com.example.Observer.view;

import com.example.Observer.model.ConcreteSubject;
import com.example.Observer.model.EmailObserver;

public class ObserverDemo {
    
    public static void main(String[] args) {
        System.out.println("NOTIFICACION OBSERVER\n");
        
        ConcreteSubject cuenta = new ConcreteSubject("1234567890", 1000.0, "Savings");
        
        EmailObserver cliente1 = new EmailObserver("cliente1@email.com", "Juan Pérez");
        
        cuenta.attach(cliente1);
        
        System.out.println("\nOPERACIONES BANCARIAS");
        
        cuenta.deposit(500.0);
        System.out.println();
        
        cuenta.withdraw(200.0);
        System.out.println();
        
        cuenta.transfer(300.0, "9876543210");
        System.out.println();
        
        System.out.println("ESTADO FINAL");
        System.out.println(cuenta);
        System.out.println("Observadores registrados: " + cuenta.getObserverCount());
        
    }
}