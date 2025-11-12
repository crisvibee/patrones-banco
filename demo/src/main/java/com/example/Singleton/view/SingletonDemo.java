package com.example.Singleton.view;

import com.example.Singleton.controller.Client;
import com.example.Singleton.model.DatabaseConnection;

public class SingletonDemo {
    
    public static void main(String[] args) {
        System.out.println("CONEXION BASE DE DATOS SINGLETON\n");
        
        // Crear múltiples clientes
        Client client1 = new Client("Cliente A");
        Client client2 = new Client("Cliente B");
        
        System.out.println("\nVerificando que todos usan la misma instancia");
        
        // Verificar que todos tienen la misma instancia
        DatabaseConnection conn1 = client1.getDatabaseConnection();
        DatabaseConnection conn2 = client2.getDatabaseConnection();
        
        System.out.println("Misma instancia cliente1 y cliente2: " + (conn1 == conn2));
        System.out.println("Misma instancia cliente2 y cliente3: " + (conn2 == conn1));

        
        System.out.println("\nRealizando operaciones con cada cliente");
        
        // Realizar operaciones con cada cliente
        client1.performDatabaseOperations();
        client2.performDatabaseOperations();

        
        System.out.println("Verificando estado compartido");
        
        // Mostrar información de conexión compartida
        System.out.println("Información conexión cliente1: " + client1.getConnectionInfo());
        System.out.println("Información conexión cliente2: " + client2.getConnectionInfo());
      
        
        System.out.println("\nIntentando cerrar conexión desde un cliente");
        
        // Intentar cerrar desde un cliente (afecta a todos)
        client1.closeConnection();
        
        System.out.println("\n=== Verificando estado después del cierre ===");
        System.out.println("Cliente1 conexión activa: " + client1.isConnectionActive());
        System.out.println("Cliente2 conexión activa: " + client2.isConnectionActive());
      
        
        System.out.println("\nIntentando reconectar");
        
        // Reconectar desde otro cliente
        client2.performDatabaseOperations();
        
    }   
}