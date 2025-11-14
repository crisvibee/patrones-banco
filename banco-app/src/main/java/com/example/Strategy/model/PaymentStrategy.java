package com.example.Strategy.model;

public interface PaymentStrategy {
    
    String pay(double amount, String paymentDetails);
    
    String getPaymentType();
}
