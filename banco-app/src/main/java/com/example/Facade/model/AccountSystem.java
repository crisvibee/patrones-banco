package com.example.Facade;

public interface AccountSystem {
    boolean verifyAccount(String accountNumber);
    double getBalance(String accountNumber);
    void updateBalance(String accountNumber, double newBalance);
    String createAccount(String accountHolder, double initialBalance);
    String getAccountHolder(String accountNumber);
}

class AccountSystemImpl implements AccountSystem {
    
    private java.util.Map<String, Double> accountBalances = new java.util.HashMap<>();
    private java.util.Map<String, String> accountHolders = new java.util.HashMap<>();
    
    
    public boolean verifyAccount(String accountNumber) {
        System.out.println("Verificando cuenta: " + accountNumber);
        return accountNumber != null && accountBalances.containsKey(accountNumber);
    }
    
    public double getBalance(String accountNumber) {
        System.out.println("Obteniendo saldo de cuenta: " + accountNumber);
        return accountBalances.getOrDefault(accountNumber, 0.0);
    }
    
   
    public void updateBalance(String accountNumber, double amount) {
        System.out.println("Actualizando saldo de cuenta " + accountNumber + " con cambio: $" + amount);
 
        double currentBalance = accountBalances.getOrDefault(accountNumber, 0.0);
        double newBalance = currentBalance + amount;
    
        accountBalances.put(accountNumber, newBalance);
        System.out.println("Nuevo saldo de cuenta " + accountNumber + ": $" + newBalance);
    }
    
    
    public String createAccount(String accountHolder, double initialBalance) {
        System.out.println("Creando cuenta para: " + accountHolder + " con saldo inicial: $" + initialBalance);
      
        String accountNumber = "ACC" + System.currentTimeMillis();
        
        accountBalances.put(accountNumber, initialBalance);
        accountHolders.put(accountNumber, accountHolder);
        
        System.out.println("Cuenta creada: " + accountNumber);
        return accountNumber;
    }
    
    public String getAccountHolder(String accountNumber) {
        System.out.println("Obteniendo titular de cuenta: " + accountNumber);
      
        return accountHolders.getOrDefault(accountNumber, "Cliente Banco Ejemplo");
    }
}
