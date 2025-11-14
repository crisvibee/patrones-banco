package com.example.Singleton.controller;

import com.example.Singleton.model.DatabaseConnection;

public class Client {
    private String clientName;
    private DatabaseConnection dbConnection;
    
    public Client(String clientName) {
        this.clientName = clientName;
        // Obtener la instancia Singleton
        this.dbConnection = DatabaseConnection.getInstance();
        System.out.println("Cliente '" + clientName + "' creado - Instancia Singleton obtenida");
    }
    
    public void performDatabaseOperations() {
        System.out.println("\n--- " + clientName + " operando ---");
        
        System.out.println("Conectando a la base de datos...");
        dbConnection.connect();
        
        System.out.println("\nEjecutando consultas:");
        dbConnection.executeQuery("SELECT * FROM accounts WHERE client_name = '" + clientName + "'");
        dbConnection.executeQuery("UPDATE accounts SET balance = balance + 100 WHERE client_name = '" + clientName + "'");
        
        System.out.println("\nEstado de conexión:");
        System.out.println("  Conectado: " + dbConnection.isConnected());
        System.out.println("  Total conexiones: " + dbConnection.getConnectionCount());
        System.out.println("  String conexión: " + dbConnection.getConnectionString());
        
        System.out.println("--- " + clientName + " terminó ---\n");
    }
    
    
    public void closeConnection() {
        System.out.println(clientName + " intentando cerrar conexión...");
        dbConnection.disconnect();
    }
    
    public boolean isConnectionActive() {
        return dbConnection.isConnected();
    }
    
    
    public String getConnectionInfo() {
        return "Cliente: " + clientName + ", " + dbConnection.toString();
    }
    
    public String getClientName() {
        return clientName;
    }
    
    
    public DatabaseConnection getDatabaseConnection() {
        return dbConnection;
    }
}
