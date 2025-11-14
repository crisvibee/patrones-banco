package com.example.Facade;

public interface NotificationSystem {
    void sendEmail(String email, String subject, String message);
    void sendSMS(String phoneNumber, String message);
    void sendPushNotification(String deviceId, String title, String body);
    void notifyTransactionSuccess(String accountNumber, String transactionType, double amount, String email);
    void notifyTransactionFailure(String accountNumber, String transactionType, double amount, String email, String reason);
    void notifyAccountCreation(String accountNumber, String accountHolder, double initialBalance, String email);
}

class NotificationSystemImpl implements NotificationSystem {
    
    
    public void sendEmail(String email, String subject, String message) {
        System.out.println("=== NOTIFICACIÓN POR EMAIL ===");
        System.out.println("Para: " + email);
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + message);
        System.out.println("=============================");
    }
    
    public void sendSMS(String phoneNumber, String message) {
        System.out.println("=== NOTIFICACIÓN POR SMS ===");
        System.out.println("Para: " + phoneNumber);
        System.out.println("Mensaje: " + message);
        System.out.println("==========================");
    }
    
    public void sendPushNotification(String deviceId, String title, String body) {
        System.out.println("=== NOTIFICACIÓN PUSH ===");
        System.out.println("Dispositivo: " + deviceId);
        System.out.println("Título: " + title);
        System.out.println("Contenido: " + body);
        System.out.println("========================");
    }
    
    public void notifyTransactionSuccess(String accountNumber, String transactionType, double amount, String email) {
        String subject = "Transacción exitosa - " + transactionType;
        String message = "Su " + transactionType.toLowerCase() + " de $" + amount + 
                        " en la cuenta " + accountNumber + " fue procesada exitamente.";
        sendEmail(email, subject, message);
        
        String smsMessage = transactionType + " exitosa: $" + amount + " en cuenta " + accountNumber.substring(accountNumber.length() - 4);
        sendSMS("+1234567890", smsMessage);
    }
    
    
    public void notifyTransactionFailure(String accountNumber, String transactionType, double amount, String email, String reason) {
        String subject = "Transacción fallida - " + transactionType;
        String message = "Su " + transactionType.toLowerCase() + " de $" + amount + 
                        " en la cuenta " + accountNumber + " no pudo ser procesada. " +
                        "Razón: " + reason;
        sendEmail(email, subject, message);
    }
    
    public void notifyAccountCreation(String accountNumber, String accountHolder, double initialBalance, String email) {
        String subject = "¡Bienvenido! Su nueva cuenta ha sido creada";
        String message = "Estimado " + accountHolder + ",\n\n" +
                        "Su nueva cuenta " + accountNumber + " ha sido creada exitosamente.\n" +
                        "Saldo inicial: $" + initialBalance + "\n\n" +
                        "Gracias por confiar en nuestro banco.";
        sendEmail(email, subject, message);
    }
}
