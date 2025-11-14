package com.example.Facade;


public class BankFacade {
    
    private AccountSystem accountSystem;
    private TransactionSystem transactionSystem;
    private NotificationSystem notificationSystem;
    
    public BankFacade() {
        this.accountSystem = new AccountSystemImpl();
        this.transactionSystem = new TransactionSystemImpl();
        this.notificationSystem = new NotificationSystemImpl();
    }
    
    public BankFacade(AccountSystem accountSystem, TransactionSystem transactionSystem, NotificationSystem notificationSystem) {
        this.accountSystem = accountSystem;
        this.transactionSystem = transactionSystem;
        this.notificationSystem = notificationSystem;
    }
    
    public String createAccount(String accountHolder, double initialBalance, String email) {
        String accountNumber = accountSystem.createAccount(accountHolder, initialBalance);
      
        if (accountSystem.verifyAccount(accountNumber)) {
     
            notificationSystem.notifyAccountCreation(accountNumber, accountHolder, initialBalance, email);
            return accountNumber;
        } else {
            throw new RuntimeException("Error al crear la cuenta");
        }
    }
    
    
    public boolean transfer(String fromAccount, String toAccount, double amount, String emailFrom, String emailTo) {
       
        if (!accountSystem.verifyAccount(fromAccount)) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Cuenta de origen no existe");
            return false;
        }
        
        if (!accountSystem.verifyAccount(toAccount)) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Cuenta de destino no existe");
            return false;
        }
        
        double currentBalance = accountSystem.getBalance(fromAccount);
        if (currentBalance < amount) {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Fondos insuficientes");
            return false;
        }
        
        boolean success = transactionSystem.processTransfer(fromAccount, toAccount, amount);
        
        if (success) {
           
            accountSystem.updateBalance(fromAccount, -amount);
            accountSystem.updateBalance(toAccount, amount);
           
            notificationSystem.notifyTransactionSuccess(fromAccount, "Transferencia", amount, emailFrom);
            notificationSystem.notifyTransactionSuccess(toAccount, "Depósito", amount, emailTo);
            
            return true;
        } else {
            notificationSystem.notifyTransactionFailure(fromAccount, "Transferencia", amount, emailFrom, "Error en el procesamiento");
            return false;
        }
    }
    
    
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
    
    public boolean withdraw(String accountNumber, double amount, String email) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            notificationSystem.notifyTransactionFailure(accountNumber, "Retiro", amount, email, "Cuenta no existe");
            return false;
        }
      
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
    
    
    public double getBalance(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return accountSystem.getBalance(accountNumber);
    }
    
   
    public String getTransactionHistory(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return transactionSystem.getTransactionHistory(accountNumber);
    }
    

    public boolean verifyAccount(String accountNumber) {
        return accountSystem.verifyAccount(accountNumber);
    }
    
    public String getAccountHolder(String accountNumber) {
        if (!accountSystem.verifyAccount(accountNumber)) {
            throw new IllegalArgumentException("Cuenta no existe: " + accountNumber);
        }
        return accountSystem.getAccountHolder(accountNumber);
    }
}
