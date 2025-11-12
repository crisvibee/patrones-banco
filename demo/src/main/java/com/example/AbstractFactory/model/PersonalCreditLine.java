package com.example.AbstractFactory.model;

public class PersonalCreditLine extends CreditLine {

    public PersonalCreditLine(String creditLineNumber, double creditLimit, double balance) {
        super(creditLineNumber, creditLimit, balance);
    }
    
    @Override
    public void approve() {
        System.out.println("Personal credit line approved");
    }

    @Override
    public void cancel() {
        System.out.println("Personal credit line cancelled");
    }

    @Override
    public void getDetails() {
        System.out.println("Personal credit line details: " + creditLineNumber + ", " + creditLimit + ", " + balance);
    }

}
