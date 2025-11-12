package com.example.AbstractFactory.model;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountNumber, double balance, String type) {
        super(accountNumber, balance, type);
    }

    @Override
    public void open() {
        System.out.println("Checking account opened");
    }

    @Override
    public void close() {
        System.out.println("Checking account closed");
    }

    @Override
    public void getDetails() {
        System.out.println("Checking account details: " + accountNumber + ", " + balance + ", " + type);
    }

}
