package com.example.AbstractFactory.model;

public abstract class CreditLine {

    protected String creditLineNumber;
    protected double creditLimit;
    protected double balance;

    public CreditLine(String creditLineNumber, double creditLimit, double balance) {
        this.creditLineNumber = creditLineNumber;
        this.creditLimit = creditLimit;
        this.balance = balance;
    }

    public String getCreditLineNumber() {
        return creditLineNumber;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getBalance() {
        return balance;
    }

    
    public double getAvailableCredit() {
        return creditLimit - balance;
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance + amount <= creditLimit) {
            balance += amount;
        }
    }

    public void deposit(double amount) {
        if (amount > 0 && balance - amount >= 0) {
            balance -= amount;
        }
    }

    public abstract void approve();
    public abstract void cancel();
    public abstract void getDetails();

}
