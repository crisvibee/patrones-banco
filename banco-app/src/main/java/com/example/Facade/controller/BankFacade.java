package com.example.Facade;

/**
 * Fachada que simplifica la interacción con los sistemas bancarios
 * Proporciona una interfaz unificada para operaciones bancarias complejas
 */
public class BankFacade {
    
    private AccountSystem accountSystem;
    private TransactionSystem transactionSystem;
    private NotificationSystem notificationSystem;
    
    /**
     * Constructor que inicializa los sistemas
     */
    public BankFacade() {
        this.accountSystem = new AccountSystemImpl();
        this.transactionSystem = new TransactionSystemImpl();
        this.notificationSystem = new NotificationSystemImpl();
    }
    
    /**
     * Constructor que permite inyectar dependencias (para testing)
     */
    public BankFacade(AccountSystem accountSystem, TransactionSystem transactionSystem, NotificationSystem notificationSystem) {
        this.accountSystem = accountSystem;
        this.transactionSystem = transactionSystem;
        this.notificationSystem = notificationSystem;
    }
    
    /**
     * Crea una nueva cuenta bancaria
     * @param accountHolder Titular de la cuenta
     * @param initialBalance Saldo inicial
     * @param email Email del cliente
     * @return Número de cuenta generado
     */
    public String createAccount(String accountHolder, double initialBalance, String email) {
        String accountNumber = accountSystem.createAccount(accountHolder, initialBalance);
        
        // Verificar que la cuenta se creó correctamente
        if (accountSystem.verifyAccount(accountNumber)) {
            // Notificar al cliente
            notificationSystem.notifyAccountCreation(accountNumber, accountHolder, initialBalance, email);
            return accountNumber;
        } else {
            throw new RuntimeException("Error al crear la cuenta");
        }
    }
    
    /**
     * Realiza una transferencia entre cuentas
     * @param fromAccount Cuenta de origen
     * @param toAccount Cuenta de destino
     * @param amount Monto a transferir
     * @param emailFrom Email del titular de la cuenta de origen
     * @param emailTo Email del titular de la cuenta de destino
     * @return true si la transferencia fue exitosa
     */
    public boolean transfer(String fromAccount, String toAccount, double amount, String emailFrom, String emailTo) {
        // Verificar que ambas cuentas existan
        if (!accountSystem.verifyAccount(fromAccount)) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Cuenta de origen no existe");
            return false;
        }
        
        if (!accountSystem.verifyAccount(toAccount)) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Cuenta de destino no existe");
            return false;
        }
        
        // Verificar fondos suficientes
        double currentBalance = accountSystem.getBalance(fromAccount);
        if (currentBalance < amount) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Fondos insuficientes");
            return false;
        }
        
        // Procesar la transferencia
        boolean success = transactionSystem.processTransfer(fromAccount, toAccount, amount);
        
        if (success) {
            // Actualizar saldos
            accountSystem.updateBalance(fromAccount, -amount);
            accountSystem.updateBalance(toAccount, amount);
            
            // Notificar a ambos clientes
            notificationSystem.notifyTransactionSuccess(fromAccount, "Transferencia", amount, emailFrom);
            notificationSystem.notifyTransactionSuccess(toAccount, "Depósito", amount, emailTo);
            
            return true;
        } else {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Error en el procesamiento");
            return false;
        }
    }
    
    /**
     * Realiza un depósito en una cuenta
     * @param accountNumber Número de cuenta
     * @param amount Monto a depositar
     * @param email Email del cliente
     * @return true si el depósito fue exitoso
     */
    public boolean deposit(String accountNumber, double amount, String email) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            notificationSystem.notifyTransactionFailure(accountNumber, "Depósito", amount, email, "Cuenta no existe");
            return false;
        }
        
        boolean success = transactionSystem.processDeposit(accountNumber, amount);
        
        if (success) {
            accountSystem.updateBalance(accountNumber, amount);
            notificationSystem.notifyTransactionSuccess(accountNumber, "Depósito", amount, email);
            return true;
        } else {
            notificationSystem.notifyTransactionFailure(accountNumber, "Depósito", amount, email, "Error en el procesamiento");
            return false;
        }
    }
    
    /**
     * Realiza un retiro de una cuenta
     * @param accountNumber Número de cuenta
     * @param amount Monto a retirar
     * @param email Email del cliente
     * @return true si el retiro fue exitoso
     */
    public boolean withdraw(String accountNumber, double amount, String email) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            notificationSystem.notifyTransactionFailure(accountNumber, "Retiro", amount, email, "Cuenta no existe");
            return false;
        }
        
        // Verificar fondos suficientes
        double currentBalance = accountSystem.getBalance(accountNumber);
        if (currentBalance < amount) {
            notificationSystem.notifyTransactionFailure(accountNumber, "Retiro", amount, email, "Fondos insuficientes");
            return false;
        }
        
        boolean success = transactionSystem.processWithdrawal(accountNumber, amount);
        
        if (success) {
            accountSystem.updateBalance(accountNumber, -amount);
            notificationSystem.notifyTransactionSuccess(accountNumber, "Retiro", amount, email);
            return true;
        } else {
            notificationSystem.notifyTransactionFailure(accountNumber, "Retiro", amount, email, "Error en el procesamiento");
            return false;
        }
    }
    
    /**
     * Consulta el saldo de una cuenta
     * @param accountNumber Número de cuenta
     * @return Saldo actual de la cuenta
     */
    public double getBalance(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return accountSystem.getBalance(accountNumber);
    }
    
    /**
     * Obtiene el historial de transacciones de una cuenta
     * @param accountNumber Número de cuenta
     * @return String con el historial de transacciones
     */
    public String getTransactionHistory(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return transactionSystem.getTransactionHistory(accountNumber);
    }
    
    /**
     * Verifica si una cuenta existe
     * @param accountNumber Número de cuenta
     * @return true si la cuenta existe
     */
    public boolean verifyAccount(String accountNumber) {
        return accountSystem.verifyAccount(accountNumber);
    }
    
    /**
     * Obtiene información del titular de la cuenta
     * @param accountNumber Número de cuenta
     * @return Nombre del titular de la cuenta
     */
    public String getAccountHolder(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return accountSystem.getAccountHolder(accountNumber);
    }
}
