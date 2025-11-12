package com.example.Command.view;

import com.example.Command.controller.TransactionInvoker;
import com.example.Command.model.Command;
import com.example.Command.model.DepositCommand;
import com.example.Command.model.TransferCommand;
import com.example.Command.model.WithdrawCommand;
import com.example.Observer.model.ConcreteSubject;

public class CommandDemo {
    
    public static void main(String[] args) {
        System.out.println("PATRON COMMAND\n");
        
        // Crear cuentas bancarias para la demostración
        ConcreteSubject cuenta1 = new ConcreteSubject("123456789", 1000.0, "Ahorros");
        ConcreteSubject cuenta2 = new ConcreteSubject("987654321", 500.0, "Corriente");
        
        System.out.println("Cuentas creadas:");
        System.out.println("Cuenta 1: " + cuenta1.getAccountNumber() + " - Tipo: " + cuenta1.getAccountType() + " - Saldo: $" + cuenta1.getBalance());
        System.out.println("Cuenta 2: " + cuenta2.getAccountNumber() + " - Tipo: " + cuenta2.getAccountType() + " - Saldo: $" + cuenta2.getBalance());
       
        
        // Crear el invocador de transacciones
        TransactionInvoker invoker = new TransactionInvoker();
        
        System.out.println("\n=== EJECUTANDO OPERACIONES ===");
        
        // Ejecutar comandos de depósito
        Command deposito1 = new DepositCommand(cuenta1, 200.0);
        invoker.executeCommand(deposito1);
        
        Command deposito2 = new DepositCommand(cuenta2, 100.0);
        invoker.executeCommand(deposito2);
        
        // Ejecutar comandos de retiro
        Command retiro1 = new WithdrawCommand(cuenta1, 150.0);
        invoker.executeCommand(retiro1);
        
        Command retiro2 = new WithdrawCommand(cuenta2, 700.0); // Este debería fallar por fondos insuficientes
        invoker.executeCommand(retiro2);
        
        // Ejecutar comandos de transferencia
        Command transferencia1 = new TransferCommand(cuenta1, cuenta2, 300.0);
        invoker.executeCommand(transferencia1);
        
        Command transferencia2 = new TransferCommand(cuenta2, cuenta1, 500.0);
        invoker.executeCommand(transferencia2);
        
        System.out.println("\nMOSTRANDO HISTORIAL");
        invoker.showCommandHistory();
        
        System.out.println("\n=== MOSTRANDO ESTADO ACTUAL DE CUENTAS ===");
        System.out.println("Cuenta 1: $" + cuenta1.getBalance());
        System.out.println("Cuenta 2: $" + cuenta2.getBalance());
        
        System.out.println("\n=== DEMOSTRACIÓN DE DESHACER/REHACER ===");
        
        // Mostrar estado de pilas antes de deshacer
        invoker.showStackStatus();
        
        // Deshacer la última operación
        invoker.undoLastCommand();
        
        // Mostrar estado después de deshacer
        System.out.println("\nEstado después de deshacer:");
        System.out.println("Cuenta 1: $" + cuenta1.getBalance());
        System.out.println("Cuenta 2: $" + cuenta2.getBalance());
        
        // Deshacer otra operación
        invoker.undoLastCommand();
        
        // Mostrar estado de pilas después de deshacer
        invoker.showStackStatus();
        
        // Rehacer la última operación deshecha
        invoker.redoLastCommand();
        
        System.out.println("\nEstado después de rehacer:");
        System.out.println("Cuenta 1: $" + cuenta1.getBalance());
        System.out.println("Cuenta 2: $" + cuenta2.getBalance());
        
        // Mostrar historial final
        System.out.println("\n=== HISTORIAL FINAL ===");
        invoker.showCommandHistory();
        
        System.out.println("\n=== ESTADO FINAL DE CUENTAS ===");
        System.out.println("Cuenta 1: $" + cuenta1.getBalance());
        System.out.println("Cuenta 2: $" + cuenta2.getBalance());
    
    }
}