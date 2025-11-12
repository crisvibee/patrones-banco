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
    
    /**
     * Verifica si una cuenta existe
     * @param accountNumber Número de cuenta
     * @return true si la cuenta existe, false en caso contrario
     */
    public boolean verifyAccount(String accountNumber) {
        System.out.println("Verificando cuenta: " + accountNumber);
        // Verificar si la cuenta existe en nuestro mapa de saldos
        return accountNumber != null && accountBalances.containsKey(accountNumber);
    }
    
    /**
     * Obtiene el saldo de una cuenta
     * @param accountNumber Número de cuenta
     * @return Saldo de la cuenta
     */
    public double getBalance(String accountNumber) {
        System.out.println("Obteniendo saldo de cuenta: " + accountNumber);
        // Retornar el saldo almacenado, o 0 si la cuenta no existe
        return accountBalances.getOrDefault(accountNumber, 0.0);
    }
    
    /**
     * Actualiza el saldo de una cuenta después de una transacción
     * @param accountNumber Número de cuenta
     * @param amount Monto a sumar (puede ser positivo o negativo)
     */
    public void updateBalance(String accountNumber, double amount) {
        System.out.println("Actualizando saldo de cuenta " + accountNumber + " con cambio: $" + amount);
        // Obtener el saldo actual y sumar el monto
        double currentBalance = accountBalances.getOrDefault(accountNumber, 0.0);
        double newBalance = currentBalance + amount;
        // Actualizar el saldo en nuestro mapa
        accountBalances.put(accountNumber, newBalance);
        System.out.println("Nuevo saldo de cuenta " + accountNumber + ": $" + newBalance);
    }
    
    /**
     * Crea una nueva cuenta
     * @param accountHolder Titular de la cuenta
     * @param initialBalance Saldo inicial
     * @return Número de cuenta generado
     */
    public String createAccount(String accountHolder, double initialBalance) {
        System.out.println("Creando cuenta para: " + accountHolder + " con saldo inicial: $" + initialBalance);
        // Generar número de cuenta
        String accountNumber = "ACC" + System.currentTimeMillis();
        
        // Almacenar la información de la cuenta
        accountBalances.put(accountNumber, initialBalance);
        accountHolders.put(accountNumber, accountHolder);
        
        System.out.println("Cuenta creada: " + accountNumber);
        return accountNumber;
    }
    
    /**
     * Obtiene el nombre del titular de una cuenta
     * @param accountNumber Número de cuenta
     * @return Nombre del titular de la cuenta
     */
    public String getAccountHolder(String accountNumber) {
        System.out.println("Obteniendo titular de cuenta: " + accountNumber);
        // Retornar el titular almacenado, o un valor por defecto si no existe
        return accountHolders.getOrDefault(accountNumber, "Cliente Banco Ejemplo");
    }
}
