package com.example.Facade.model;

import com.example.Facade.TransactionSystem;

public class TransactionSystemImpl implements TransactionSystem {
    
    public boolean processTransfer(String fromAccount, String toAccount, double amount) {
        System.out.println("Procesando transferencia de $" + amount + " desde " + fromAccount + " hacia " + toAccount);
        
        if (amount <= 0) {
            System.out.println("Error: Monto debe ser mayor a cero");
            return false;
        }
        
        if (fromAccount.equals(toAccount)) {
            System.out.println("Error: No se puede transferir a la misma cuenta");
            return false;
        }
        
       
        System.out.println("Transferencia procesada exitosamente");
        return true;
    }
    
    public boolean processDeposit(String accountNumber, double amount) {
        System.out.println("Procesando depósito de $" + amount + " en cuenta " + accountNumber);
        
        if (amount <= 0) {
            System.out.println("Error: Monto debe ser mayor a cero");
            return false;
        }
        
        System.out.println("Depósito procesado exitosamente");
        return true;
    }
    

    public boolean processWithdrawal(String accountNumber, double amount) {
        System.out.println("Procesando retiro de $" + amount + " de cuenta " + accountNumber);
        
        if (amount <= 0) {
            System.out.println("Error: Monto debe ser mayor a cero");
            return false;
        }
        
        System.out.println("Retiro procesado exitosamente");
        return true;
    }
    
    public String getTransactionHistory(String accountNumber) {
        System.out.println("Obteniendo historial de transacciones para cuenta: " + accountNumber);
        return "Historial de transacciones para cuenta " + accountNumber + 
               "\n- 01/11/2024: Depósito +$500.00" +
               "\n- 05/11/2024: Transferencia -$200.00" +
               "\n- 10/11/2024: Retiro -$100.00";
    }
}