package com.example.AbstractFactory.model;

public class BusinessCreditLine extends CreditLine {

    public BusinessCreditLine(String creditLineNumber, double creditLimit, double balance) {
        super(creditLineNumber, creditLimit, balance);
    }

    @Override
    public void approve() {
        System.out.println("Business credit line approved");
    }

    @Override
    public void cancel() {
        System.out.println("Business credit line cancelled");
    }

    @Override
    public void getDetails() {
        System.out.println("Business credit line details: " + creditLineNumber + ", " + creditLimit + ", " + balance);
    }
}
