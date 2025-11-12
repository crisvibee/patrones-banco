package com.example.AbstractFactory.model;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, double balance, String type) {
        super(accountNumber, balance, type);
    }

    @Override
    public void open() {
        System.out.println("Savings account opened");
    }

    @Override
    public void close() {
        System.out.println("Savings account closed");
    }

    @Override
    public void getDetails() {
        System.out.println("Savings account details: " + accountNumber + ", " + balance + ", " + type);
    }
}
