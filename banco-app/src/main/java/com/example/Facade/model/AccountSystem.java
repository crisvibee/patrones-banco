package com.example.Facade;

public interface AccountSystem {
    boolean verifyAccount(String accountNumber);
    double getBalance(String accountNumber);
    void updateBalance(String accountNumber, double newBalance);
    String createAccount(String accountHolder, double initialBalance);
    String getAccountHolder(String accountNumber);
}
