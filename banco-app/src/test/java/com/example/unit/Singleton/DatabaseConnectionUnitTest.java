package com.example.unit.Singleton;

import com.example.Singleton.model.DatabaseConnection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionUnitTest {
    
    @Test
    public void testSingletonInstanceCreation() {
      
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        
        assertNotNull(instance1, "La instancia Singleton no debería ser nula");
        assertTrue(instance1 instanceof DatabaseConnection, "La instancia debería ser de tipo DatabaseConnection");
    }
    
    @Test
    public void testSingletonReturnsSameInstance() {
  
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        assertSame(instance1, instance2, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
        assertSame(instance2, instance3, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
        assertSame(instance1, instance3, "Todas las llamadas a getInstance() deberían retornar la misma instancia");
    }
    
    @Test
    public void testSingletonConnectionString() {
 
        DatabaseConnection instance = DatabaseConnection.getInstance();
     
        String connectionString = instance.getConnectionString();
     
        assertNotNull(connectionString, "El string de conexión no debería ser nulo");
        assertEquals("jdbc:mysql://localhost:3306/bankdb", connectionString, 
            "El string de conexión debería coincidir con el esperado");
    }
    
    @Test
    public void testSingletonInitialState() {
     
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.disconnect(); 
        
        assertFalse(instance.isConnected(), "La conexión debería estar desconectada después de reset");
        
    }
    
    @Test
    public void testSingletonConnectDisconnect() {
      
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        instance.disconnect();
        int initialCount = instance.getConnectionCount();
        
        instance.connect();
        
        assertTrue(instance.isConnected(), "La conexión debería estar activa después de connect()");
        assertEquals(initialCount + 1, instance.getConnectionCount(), "El contador de conexiones debería incrementarse en 1 desde el estado inicial");
        
        instance.disconnect();
      
        assertFalse(instance.isConnected(), "La conexión debería estar inactiva después de disconnect()");
    }
    
    @Test
    public void testSingletonMultipleConnections() {
     
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        if (instance.isConnected()) {
            instance.disconnect();
        }
        
        int initialCount = instance.getConnectionCount();
        
        instance.connect();
        instance.disconnect();
        instance.connect();
        instance.disconnect();
        instance.connect();
        
        assertTrue(instance.isConnected(), "La conexión debería estar activa después de la última conexión");
        assertEquals(initialCount + 3, instance.getConnectionCount(), 
            "El contador debería incrementarse en 3 conexiones desde el estado inicial de esta prueba");
    }
    
    @Test
    public void testSingletonExecuteQueryWhenConnected() {
 
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.connect();
        
        assertDoesNotThrow(() -> instance.executeQuery("SELECT * FROM accounts"),
            "executeQuery() no debería lanzar excepciones cuando está conectado");
    }
    
    @Test
    public void testSingletonExecuteQueryWhenDisconnected() {
      
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.disconnect(); 
        
        assertDoesNotThrow(() -> instance.executeQuery("SELECT * FROM accounts"),
            "executeQuery() no debería lanzar excepciones cuando está desconectado");
    }
    
    @Test
    public void testSingletonToString() {
    
        DatabaseConnection instance = DatabaseConnection.getInstance();
        instance.connect();
        
        String toStringResult = instance.toString();
       
        assertNotNull(toStringResult, "toString() no debería retornar nulo");
        assertTrue(toStringResult.contains("DatabaseConnection"), 
            "toString() debería incluir el nombre de la clase");
        assertTrue(toStringResult.contains("jdbc:mysql://localhost:3306/bankdb"),
            "toString() debería incluir el string de conexión");
        assertTrue(toStringResult.contains("isConnected=true"),
            "toString() debería indicar que está conectado");
    }
    
    @Test
    public void testSingletonThreadSafety() {
      
        final int NUM_THREADS = 10;
        final DatabaseConnection[] instances = new DatabaseConnection[NUM_THREADS];
        
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < NUM_THREADS; i++) {
            assertSame(firstInstance, instances[i], 
                "Todas las instancias obtenidas en diferentes hilos deberían ser la misma");
        }
    }
}