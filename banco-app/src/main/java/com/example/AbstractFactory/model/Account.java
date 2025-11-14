package com.example.AbstractFactory.model;

public abstract class Account {

    protected String accountNumber;
    protected double balance;
    protected String type;

    public Account(String accountNumber, double balance, String type) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
    }

  
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getType() {
        return type;
    }

    public abstract void open();
    public abstract void close();
    public abstract void getDetails();

}
