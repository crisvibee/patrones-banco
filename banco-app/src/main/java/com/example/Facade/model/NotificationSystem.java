package com.example.Facade;

public interface NotificationSystem {
    void sendEmail(String email, String subject, String message);
    void sendSMS(String phoneNumber, String message);
    void sendPushNotification(String deviceId, String title, String body);
    void notifyTransactionSuccess(String accountNumber, String transactionType, double amount, String email);
    void notifyTransactionFailure(String accountNumber, String transactionType, double amount, String email, String reason);
    void notifyAccountCreation(String accountNumber, String accountHolder, double initialBalance, String email);
}
