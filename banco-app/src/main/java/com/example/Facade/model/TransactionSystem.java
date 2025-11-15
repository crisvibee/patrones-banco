package com.example.Facade;

public interface TransactionSystem {
    boolean processTransfer(String fromAccount, String toAccount, double amount);
    boolean processDeposit(String accountNumber, double amount);
    boolean processWithdrawal(String accountNumber, double amount);
    String getTransactionHistory(String accountNumber);
}
