package com.example.integration.Singleton;

import com.example.Singleton.model.DatabaseConnection;
import com.example.Singleton.controller.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingletonIntegrationTest {
    
    private Client client1;
    private Client client2;
    private Client client3;
    
    @BeforeEach
    public void setUp() {
       
        client1 = new Client("Cliente Integración A");
        client2 = new Client("Cliente Integración B");
        client3 = new Client("Cliente Integración C");
    }
    
    @Test
    public void testMultipleClientsShareSameSingletonInstance() {
       
        DatabaseConnection conn1 = client1.getDatabaseConnection();
        DatabaseConnection conn2 = client2.getDatabaseConnection();
        DatabaseConnection conn3 = client3.getDatabaseConnection();
       
        assertSame(conn1, conn2, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn2, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn1, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
    }
    
    @Test
    public void testSingletonStateSharingBetweenClients() {
  
        DatabaseConnection sharedConnection = client1.getDatabaseConnection();
        
        client1.performDatabaseOperations();
        boolean isConnectedAfterClient1 = client1.isConnectionActive();
        
        assertEquals(isConnectedAfterClient1, client2.isConnectionActive(),
            "Todos los clientes deberían ver el mismo estado de conexión");
        assertEquals(isConnectedAfterClient1, client3.isConnectionActive(),
            "Todos los clientes deberían ver el mismo estado de conexión");
        assertTrue(sharedConnection.isConnected(), 
            "La conexión Singleton debería estar activa después de las operaciones");
    }
    
    @Test
    public void testSingletonConnectionCountSharedAcrossClients() {
     
        DatabaseConnection sharedConnection = client1.getDatabaseConnection();
        if (sharedConnection.isConnected()) {
            client1.closeConnection();
        }
        int initialConnectionCount = sharedConnection.getConnectionCount();
        
     
        client1.performDatabaseOperations(); 
        int countAfterClient1 = sharedConnection.getConnectionCount();
        
        client2.performDatabaseOperations(); 
        int countAfterClient2 = sharedConnection.getConnectionCount();
        
        client3.performDatabaseOperations(); 
        int countAfterClient3 = sharedConnection.getConnectionCount();

        assertEquals(initialConnectionCount + 1, countAfterClient1,
            "El contador de conexiones debería incrementarse con la primera conexión real");
        assertEquals(countAfterClient1, countAfterClient2,
            "El contador no debería cambiar con conexiones adicionales cuando ya está conectado");
        assertEquals(countAfterClient2, countAfterClient3,
            "El contador no debería cambiar con conexiones adicionales cuando ya está conectado");
    }
    
    @Test
    public void testSingletonConnectionClosureAffectsAllClients() {
  
        client1.performDatabaseOperations(); 
        assertTrue(client1.isConnectionActive(), "La conexión debería estar activa inicialmente");
        
        client1.closeConnection();
        
        assertFalse(client1.isConnectionActive(), 
            "Cliente 1 debería ver la conexión cerrada después de closeConnection()");
        assertFalse(client2.isConnectionActive(), 
            "Cliente 2 debería ver la conexión cerrada (estado compartido)");
        assertFalse(client3.isConnectionActive(), 
            "Cliente 3 debería ver la conexión cerrada (estado compartido)");
    }
    
    @Test
    public void testSingletonReconnectionAfterClosure() {
      
        client1.performDatabaseOperations(); 
        client1.closeConnection(); 
        
        client2.performDatabaseOperations(); 
        
        assertTrue(client1.isConnectionActive(), 
            "Cliente 1 debería ver la conexión reactivada después de reconexión");
        assertTrue(client2.isConnectionActive(), 
            "Cliente 2 debería ver la conexión reactivada");
        assertTrue(client3.isConnectionActive(), 
            "Cliente 3 debería ver la conexión reactivada");
    }
    
    @Test
    public void testSingletonSharedConnectionInformation() {
      
        client1.performDatabaseOperations();
        
        String info1 = client1.getConnectionInfo();
        String info2 = client2.getConnectionInfo();
        String info3 = client3.getConnectionInfo();
        
        assertNotNull(info1, "La información de conexión no debería ser nula");
        assertNotNull(info2, "La información de conexión no debería ser nula");
        assertNotNull(info3, "La información de conexión no debería ser nula");
        
        assertTrue(info1.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
        assertTrue(info2.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
        assertTrue(info3.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
       
        assertTrue(info1.contains("isConnected=true") || info1.contains("conectado"),
            "La información debería indicar estado conectado");
        assertTrue(info2.contains("isConnected=true") || info2.contains("conectado"),
            "La información debería indicar estado conectado");
        assertTrue(info3.contains("isConnected=true") || info3.contains("conectado"),
            "La información debería indicar estado conectado");
    }
    
    @Test
    public void testSingletonWithConcurrentClientOperations() {
     
        final int NUM_OPERATIONS = 5;
        final boolean[] operationsCompleted = new boolean[NUM_OPERATIONS];
       
        Thread[] threads = new Thread[NUM_OPERATIONS];
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            final int index = i;
            final Client currentClient = (i % 2 == 0) ? client1 : client2;
            
            threads[i] = new Thread(() -> {
                try {
                  
                    if (index % 3 == 0) {
                        currentClient.performDatabaseOperations();
                    } else if (index % 3 == 1) {
                      
                        Thread.sleep(10);
                        currentClient.performDatabaseOperations();
                    } else {
                   
                        currentClient.isConnectionActive();
                    }
                    operationsCompleted[index] = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
        
      
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            assertTrue(operationsCompleted[i], 
                "Todas las operaciones concurrentes deberían completarse exitosamente");
        }
        
      
        assertSame(client1.getDatabaseConnection(), client2.getDatabaseConnection(),
            "La instancia Singleton debería mantenerse única después de operaciones concurrentes");
        assertSame(client2.getDatabaseConnection(), client3.getDatabaseConnection(),
            "La instancia Singleton debería mantenerse única después de operaciones concurrentes");
    }
    
    @Test
    public void testSingletonPersistenceAcrossMultipleOperations() {
       
        int initialConnectionCount = client1.getDatabaseConnection().getConnectionCount();
        
        for (int i = 0; i < 3; i++) {
            client1.performDatabaseOperations();
            client2.performDatabaseOperations();
            client1.closeConnection();
            client3.performDatabaseOperations();
        }
        
        final int finalConnectionCount = client1.getDatabaseConnection().getConnectionCount();
        
     
        assertEquals(initialConnectionCount + 3, finalConnectionCount,
            "Debería haber 3 conexiones reales después de 3 ciclos completos");
        
        assertSame(client1.getDatabaseConnection(), client2.getDatabaseConnection(),
            "Las instancias deberían seguir siendo la misma después de múltiples operaciones");
        assertSame(client2.getDatabaseConnection(), client3.getDatabaseConnection(),
            "Las instancias deberían seguir siendo la misma después de múltiples operaciones");
    }
}