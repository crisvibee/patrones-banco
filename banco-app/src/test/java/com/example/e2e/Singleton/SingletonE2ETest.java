package com.example.e2e.Singleton;

import com.example.Singleton.model.DatabaseConnection;
import com.example.Singleton.controller.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingletonE2ETest {
    
    private Client bankingClient;
    private Client reportingClient;
    private Client adminClient;
    
    @BeforeEach
    public void setUp() {
        bankingClient = new Client("Sistema Bancario");
        reportingClient = new Client("Sistema de Reportes");
        adminClient = new Client("Administrador");
    }
    
    @Test
    public void testCompleteSingletonLifecycleInBankingScenario() {
        DatabaseConnection bankingConnection = bankingClient.getDatabaseConnection();
        DatabaseConnection reportingConnection = reportingClient.getDatabaseConnection();
        DatabaseConnection adminConnection = adminClient.getDatabaseConnection();
        
        assertSame(bankingConnection, reportingConnection);
        assertSame(reportingConnection, adminConnection);
        
        assertFalse(bankingConnection.isConnected());
        assertEquals(0, bankingConnection.getConnectionCount());
        
        bankingClient.performDatabaseOperations();
        
        assertTrue(bankingClient.isConnectionActive());
        assertTrue(reportingClient.isConnectionActive());
        assertTrue(adminClient.isConnectionActive());
        
        assertEquals(1, bankingConnection.getConnectionCount());
        
        bankingClient.performDatabaseOperations();
        reportingClient.performDatabaseOperations();
        
        assertEquals(1, bankingConnection.getConnectionCount());
        
        adminClient.closeConnection();
        
        assertFalse(bankingClient.isConnectionActive());
        assertFalse(reportingClient.isConnectionActive());
        assertFalse(adminClient.isConnectionActive());
        
        bankingClient.performDatabaseOperations();
        
        assertEquals(2, bankingConnection.getConnectionCount());
        
        assertTrue(bankingClient.isConnectionActive());
        assertTrue(reportingClient.isConnectionActive());
        assertTrue(adminClient.isConnectionActive());
        
        DatabaseConnection finalBankingConnection = bankingClient.getDatabaseConnection();
        DatabaseConnection finalReportingConnection = reportingClient.getDatabaseConnection();
        DatabaseConnection finalAdminConnection = adminClient.getDatabaseConnection();
        
        assertSame(finalBankingConnection, finalReportingConnection);
        assertSame(finalReportingConnection, finalAdminConnection);
        assertSame(bankingConnection, finalBankingConnection);
        
        String bankingInfo = bankingClient.getConnectionInfo();
        String reportingInfo = reportingClient.getConnectionInfo();
        String adminInfo = adminClient.getConnectionInfo();
        
        assertTrue(bankingInfo.contains("jdbc:mysql://localhost:3306/bankdb"));
        assertTrue(reportingInfo.contains("jdbc:mysql://localhost:3306/bankdb"));
        assertTrue(adminInfo.contains("jdbc:mysql://localhost:3306/bankdb"));
        
        assertEquals(finalBankingConnection.isConnected(), finalReportingConnection.isConnected());
        assertEquals(finalReportingConnection.isConnected(), finalAdminConnection.isConnected());
    }
    
    @Test
    public void testSingletonUnderHeavyLoadScenario() {
        final int NUM_CLIENTS = 5;
        final int OPERATIONS_PER_CLIENT = 10;
        
        Client[] clients = new Client[NUM_CLIENTS];
        
        for (int i = 0; i < NUM_CLIENTS; i++) {
            clients[i] = new Client("Client-" + (i + 1));
        }
        
        Thread[] threads = new Thread[NUM_CLIENTS];
        
        for (int i = 0; i < NUM_CLIENTS; i++) {
            final Client currentClient = clients[i];
            final int clientIndex = i;
            
            threads[i] = new Thread(() -> {
                try {
                    for (int op = 0; op < OPERATIONS_PER_CLIENT; op++) {
                        if (op % 4 == 0) {
                            currentClient.performDatabaseOperations();
                        } else if (op % 4 == 1) {
                            DatabaseConnection conn = currentClient.getDatabaseConnection();
                            conn.executeQuery("SELECT balance FROM accounts WHERE client_id = " + clientIndex);
                        } else if (op % 4 == 2) {
                            currentClient.isConnectionActive();
                        } else {
                            Thread.sleep(2);
                            currentClient.getConnectionInfo();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        DatabaseConnection firstInstance = clients[0].getDatabaseConnection();
        for (int i = 1; i < NUM_CLIENTS; i++) {
            assertSame(firstInstance, clients[i].getDatabaseConnection());
        }
        
        assertNotNull(firstInstance);
        assertTrue(firstInstance instanceof DatabaseConnection);
        
        assertEquals("jdbc:mysql://localhost:3306/bankdb", firstInstance.getConnectionString());
        assertTrue(firstInstance.getConnectionCount() >= 1);
    }
    
    @Test
    public void testSingletonRecoveryAfterFailureScenario() {
        bankingClient.performDatabaseOperations();
        int initialConnectionCount = bankingClient.getDatabaseConnection().getConnectionCount();
        
        adminClient.closeConnection();
        assertFalse(bankingClient.isConnectionActive());
        
        bankingClient.performDatabaseOperations();
        reportingClient.performDatabaseOperations();
        
        int finalConnectionCount = bankingClient.getDatabaseConnection().getConnectionCount();
        assertEquals(initialConnectionCount + 1, finalConnectionCount);
        
        assertTrue(bankingClient.isConnectionActive());
        assertTrue(reportingClient.isConnectionActive());
        assertTrue(adminClient.isConnectionActive());
        
        assertSame(bankingClient.getDatabaseConnection(), reportingClient.getDatabaseConnection());
        assertSame(reportingClient.getDatabaseConnection(), adminClient.getDatabaseConnection());
        
        DatabaseConnection conn = bankingClient.getDatabaseConnection();
        assertTrue(conn.isConnected());
        assertTrue(conn.getConnectionCount() >= 2);
    }
    
    @Test
    public void testSingletonCrossComponentIntegration() {
        Client transactionProcessor = new Client("TransactionProcessor");
        transactionProcessor.performDatabaseOperations();
        
        Client reportGenerator = new Client("ReportGenerator");
        reportGenerator.performDatabaseOperations();
        
        Client monitoringService = new Client("MonitoringService");
        boolean initialMonitoringState = monitoringService.isConnectionActive();
        
        Client auditSystem = new Client("AuditSystem");
        String auditInfo = auditSystem.getConnectionInfo();
        
        DatabaseConnection txConn = transactionProcessor.getDatabaseConnection();
        DatabaseConnection reportConn = reportGenerator.getDatabaseConnection();
        DatabaseConnection monitorConn = monitoringService.getDatabaseConnection();
        DatabaseConnection auditConn = auditSystem.getDatabaseConnection();
        
        assertSame(txConn, reportConn);
        assertSame(reportConn, monitorConn);
        assertSame(monitorConn, auditConn);
        
        assertEquals(txConn.isConnected(), reportConn.isConnected());
        assertEquals(reportConn.isConnected(), monitorConn.isConnected());
        assertEquals(monitorConn.isConnected(), auditConn.isConnected());
        
        assertTrue(auditInfo.contains(txConn.getConnectionString()));
        
        int connectionCount = txConn.getConnectionCount();
        assertTrue(connectionCount >= 1 && connectionCount <= 2);
    }
}