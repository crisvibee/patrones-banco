package com.example.integration.Singleton;

import com.example.Singleton.model.DatabaseConnection;
import com.example.Singleton.controller.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para el patrón Singleton
 * Prueba la interacción entre DatabaseConnection (Singleton) y Client
 */
public class SingletonIntegrationTest {
    
    private Client client1;
    private Client client2;
    private Client client3;
    
    @BeforeEach
    public void setUp() {
        // Crear múltiples clientes que compartirán la misma instancia Singleton
        client1 = new Client("Cliente Integración A");
        client2 = new Client("Cliente Integración B");
        client3 = new Client("Cliente Integración C");
    }
    
    @Test
    public void testMultipleClientsShareSameSingletonInstance() {
        // Arrange & Act - Obtener instancias de conexión de diferentes clientes
        DatabaseConnection conn1 = client1.getDatabaseConnection();
        DatabaseConnection conn2 = client2.getDatabaseConnection();
        DatabaseConnection conn3 = client3.getDatabaseConnection();
        
        // Assert - Todas las conexiones deberían ser la misma instancia Singleton
        assertSame(conn1, conn2, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn2, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
        assertSame(conn1, conn3, "Clientes diferentes deberían compartir la misma instancia Singleton");
    }
    
    @Test
    public void testSingletonStateSharingBetweenClients() {
        // Arrange
        DatabaseConnection sharedConnection = client1.getDatabaseConnection();
        
        // Act - Un cliente conecta la base de datos
        client1.performDatabaseOperations();
        boolean isConnectedAfterClient1 = client1.isConnectionActive();
        
        // Assert - Todos los clientes deberían ver el mismo estado de conexión
        assertEquals(isConnectedAfterClient1, client2.isConnectionActive(),
            "Todos los clientes deberían ver el mismo estado de conexión");
        assertEquals(isConnectedAfterClient1, client3.isConnectionActive(),
            "Todos los clientes deberían ver el mismo estado de conexión");
        assertTrue(sharedConnection.isConnected(), 
            "La conexión Singleton debería estar activa después de las operaciones");
    }
    
    @Test
    public void testSingletonConnectionCountSharedAcrossClients() {
        // Arrange
        DatabaseConnection sharedConnection = client1.getDatabaseConnection();
        int initialConnectionCount = sharedConnection.getConnectionCount();
        
        // Act - Múltiples clientes realizan operaciones de conexión
        client1.performDatabaseOperations(); // Conecta
        int countAfterClient1 = sharedConnection.getConnectionCount();
        
        client2.performDatabaseOperations(); // Ya está conectado, no debería incrementar
        int countAfterClient2 = sharedConnection.getConnectionCount();
        
        client3.performDatabaseOperations(); // Ya está conectado, no debería incrementar
        int countAfterClient3 = sharedConnection.getConnectionCount();
        
        // Assert - El contador debería incrementarse solo con la primera conexión real
        assertEquals(initialConnectionCount + 1, countAfterClient1,
            "El contador de conexiones debería incrementarse con la primera conexión real");
        assertEquals(countAfterClient1, countAfterClient2,
            "El contador no debería cambiar con conexiones adicionales cuando ya está conectado");
        assertEquals(countAfterClient2, countAfterClient3,
            "El contador no debería cambiar con conexiones adicionales cuando ya está conectado");
    }
    
    @Test
    public void testSingletonConnectionClosureAffectsAllClients() {
        // Arrange
        client1.performDatabaseOperations(); // Conectar primero
        assertTrue(client1.isConnectionActive(), "La conexión debería estar activa inicialmente");
        
        // Act - Un cliente cierra la conexión
        client1.closeConnection();
        
        // Assert - Todos los clientes deberían ver la conexión cerrada
        assertFalse(client1.isConnectionActive(), 
            "Cliente 1 debería ver la conexión cerrada después de closeConnection()");
        assertFalse(client2.isConnectionActive(), 
            "Cliente 2 debería ver la conexión cerrada (estado compartido)");
        assertFalse(client3.isConnectionActive(), 
            "Cliente 3 debería ver la conexión cerrada (estado compartido)");
    }
    
    @Test
    public void testSingletonReconnectionAfterClosure() {
        // Arrange
        client1.performDatabaseOperations(); // Conectar
        client1.closeConnection(); // Desconectar
        
        // Act - Otro cliente intenta reconectar
        client2.performDatabaseOperations(); // Debería reconectar
        
        // Assert - Todos los clientes deberían ver la conexión reactivada
        assertTrue(client1.isConnectionActive(), 
            "Cliente 1 debería ver la conexión reactivada después de reconexión");
        assertTrue(client2.isConnectionActive(), 
            "Cliente 2 debería ver la conexión reactivada");
        assertTrue(client3.isConnectionActive(), 
            "Cliente 3 debería ver la conexión reactivada");
    }
    
    @Test
    public void testSingletonSharedConnectionInformation() {
        // Arrange
        client1.performDatabaseOperations();
        
        // Act - Obtener información de conexión de diferentes clientes
        String info1 = client1.getConnectionInfo();
        String info2 = client2.getConnectionInfo();
        String info3 = client3.getConnectionInfo();
        
        // Assert - Toda la información debería ser consistente
        assertNotNull(info1, "La información de conexión no debería ser nula");
        assertNotNull(info2, "La información de conexión no debería ser nula");
        assertNotNull(info3, "La información de conexión no debería ser nula");
        
        // Todas deberían contener el mismo string de conexión
        assertTrue(info1.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
        assertTrue(info2.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
        assertTrue(info3.contains("jdbc:mysql://localhost:3306/bankdb"),
            "La información debería incluir el string de conexión");
        
        // Todas deberían indicar que está conectado
        assertTrue(info1.contains("isConnected=true") || info1.contains("conectado"),
            "La información debería indicar estado conectado");
        assertTrue(info2.contains("isConnected=true") || info2.contains("conectado"),
            "La información debería indicar estado conectado");
        assertTrue(info3.contains("isConnected=true") || info3.contains("conectado"),
            "La información debería indicar estado conectado");
    }
    
    @Test
    public void testSingletonWithConcurrentClientOperations() {
        // Arrange
        final int NUM_OPERATIONS = 5;
        final boolean[] operationsCompleted = new boolean[NUM_OPERATIONS];
        
        // Act - Ejecutar operaciones concurrentes desde diferentes clientes
        Thread[] threads = new Thread[NUM_OPERATIONS];
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            final int index = i;
            final Client currentClient = (i % 2 == 0) ? client1 : client2;
            
            threads[i] = new Thread(() -> {
                try {
                    // Simular diferentes tipos de operaciones
                    if (index % 3 == 0) {
                        currentClient.performDatabaseOperations();
                    } else if (index % 3 == 1) {
                        // Pequeña pausa antes de operar
                        Thread.sleep(10);
                        currentClient.performDatabaseOperations();
                    } else {
                        // Verificar estado
                        currentClient.isConnectionActive();
                    }
                    operationsCompleted[index] = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Assert - Todas las operaciones deberían completarse sin excepciones
        for (int i = 0; i < NUM_OPERATIONS; i++) {
            assertTrue(operationsCompleted[i], 
                "Todas las operaciones concurrentes deberían completarse exitosamente");
        }
        
        // El Singleton debería mantenerse consistente
        assertSame(client1.getDatabaseConnection(), client2.getDatabaseConnection(),
            "La instancia Singleton debería mantenerse única después de operaciones concurrentes");
        assertSame(client2.getDatabaseConnection(), client3.getDatabaseConnection(),
            "La instancia Singleton debería mantenerse única después de operaciones concurrentes");
    }
    
    @Test
    public void testSingletonPersistenceAcrossMultipleOperations() {
        // Arrange
        int initialConnectionCount = client1.getDatabaseConnection().getConnectionCount();
        
        // Act - Realizar múltiples ciclos de operaciones
        for (int i = 0; i < 3; i++) {
            client1.performDatabaseOperations();
            client2.performDatabaseOperations();
            client1.closeConnection();
            client3.performDatabaseOperations();
        }
        
        final int finalConnectionCount = client1.getDatabaseConnection().getConnectionCount();
        
        // Assert - El Singleton debería mantener su estado consistentemente
        // Debería haber 3 conexiones reales (una por cada performDatabaseOperations() después de closeConnection)
        assertEquals(initialConnectionCount + 3, finalConnectionCount,
            "Debería haber 3 conexiones reales después de 3 ciclos completos");
        
        // Todas las instancias deberían seguir siendo la misma
        assertSame(client1.getDatabaseConnection(), client2.getDatabaseConnection(),
            "Las instancias deberían seguir siendo la misma después de múltiples operaciones");
        assertSame(client2.getDatabaseConnection(), client3.getDatabaseConnection(),
            "Las instancias deberían seguir siendo la misma después de múltiples operaciones");
    }
}