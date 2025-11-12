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
    
    /**
     * Envía notificación por email
     * @param email Dirección de email
     * @param subject Asunto del mensaje
     * @param message Contenido del mensaje
     */
    public void sendEmail(String email, String subject, String message) {
        System.out.println("=== NOTIFICACIÓN POR EMAIL ===");
        System.out.println("Para: " + email);
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + message);
        System.out.println("=============================");
    }
    
    /**
     * Envía notificación por SMS
     * @param phoneNumber Número de teléfono
     * @param message Contenido del mensaje
     */
    public void sendSMS(String phoneNumber, String message) {
        System.out.println("=== NOTIFICACIÓN POR SMS ===");
        System.out.println("Para: " + phoneNumber);
        System.out.println("Mensaje: " + message);
        System.out.println("==========================");
    }
    
    /**
     * Envía notificación push
     * @param deviceId ID del dispositivo
     * @param title Título de la notificación
     * @param body Cuerpo de la notificación
     */
    public void sendPushNotification(String deviceId, String title, String body) {
        System.out.println("=== NOTIFICACIÓN PUSH ===");
        System.out.println("Dispositivo: " + deviceId);
        System.out.println("Título: " + title);
        System.out.println("Contenido: " + body);
        System.out.println("========================");
    }
    
    /**
     * Notifica sobre una transacción exitosa
     * @param accountNumber Número de cuenta
     * @param transactionType Tipo de transacción
     * @param amount Monto de la transacción
     * @param email Email del cliente
     */
    public void notifyTransactionSuccess(String accountNumber, String transactionType, double amount, String email) {
        String subject = "Transacción exitosa - " + transactionType;
        String message = "Su " + transactionType.toLowerCase() + " de $" + amount + 
                        " en la cuenta " + accountNumber + " fue procesada exitamente.";
        sendEmail(email, subject, message);
        
        // También enviar SMS
        String smsMessage = transactionType + " exitosa: $" + amount + " en cuenta " + accountNumber.substring(accountNumber.length() - 4);
        sendSMS("+1234567890", smsMessage);
    }
    
    /**
     * Notifica sobre una transacción fallida
     * @param accountNumber Número de cuenta
     * @param transactionType Tipo de transacción
     * @param amount Monto de la transacción
     * @param email Email del cliente
     * @param reason Razón del fallo
     */
    public void notifyTransactionFailure(String accountNumber, String transactionType, double amount, String email, String reason) {
        String subject = "Transacción fallida - " + transactionType;
        String message = "Su " + transactionType.toLowerCase() + " de $" + amount + 
                        " en la cuenta " + accountNumber + " no pudo ser procesada. " +
                        "Razón: " + reason;
        sendEmail(email, subject, message);
    }
    
    /**
     * Notifica creación de nueva cuenta
     * @param accountNumber Número de cuenta
     * @param accountHolder Titular de la cuenta
     * @param initialBalance Saldo inicial
     * @param email Email del cliente
     */
    public void notifyAccountCreation(String accountNumber, String accountHolder, double initialBalance, String email) {
        String subject = "¡Bienvenido! Su nueva cuenta ha sido creada";
        String message = "Estimado " + accountHolder + ",\n\n" +
                        "Su nueva cuenta " + accountNumber + " ha sido creada exitosamente.\n" +
                        "Saldo inicial: $" + initialBalance + "\n\n" +
                        "Gracias por confiar en nuestro banco.";
        sendEmail(email, subject, message);
    }
}
