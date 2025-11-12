package com.example.Observer.model;

public class ConcreteSubject extends Subject {
    private String accountNumber;
    private double balance;
    private String accountType;

    public ConcreteSubject(String accountNumber, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            String message = "Depósito: +$" + amount + " en cuenta " + accountNumber + ". Nuevo saldo: $" + balance;
            notifyObservers(message);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            String message = "Retiro: -$" + amount + " de cuenta " + accountNumber + ". Nuevo saldo: $" + balance;
            notifyObservers(message);
        } else if (amount > balance) {
            String message = "Intento de retiro: -$" + amount + " de cuenta " + accountNumber + ". Fondos insuficientes. Saldo actual: $" + balance;
            notifyObservers(message);
        }
    }

    public void transfer(double amount, String destinationAccount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            String message = "Transferencia: -$" + amount + " de cuenta " + accountNumber + " a cuenta " + destinationAccount + ". Nuevo saldo: $" + balance;
            notifyObservers(message);
        }
    }

    public void updateAccountType(String newType) {
        String oldType = this.accountType;
        this.accountType = newType;
        String message = "Cambio de tipo de cuenta: " + accountNumber + " de " + oldType + " a " + newType;
        notifyObservers(message);
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        return "Cuenta: " + accountNumber + " | Tipo: " + accountType + " | Saldo: $" + balance;
    }
}
