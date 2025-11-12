package com.example.unit.Command;

import com.example.Command.view.CommandDemo;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for CommandDemo class
 */
public class CommandDemoUnitTest {

    @Test
    public void testCommandDemoMainMethod() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Execute the main method
            CommandDemo.main(new String[]{});
            
            // Capture the output
            String output = outputStream.toString();
            
            // Verify that the demo runs without exceptions and produces expected output
            assertTrue(output.contains("PATRON COMMAND"), "Should contain pattern name");
            assertTrue(output.contains("EJECUTANDO OPERACIONES"), "Should contain operations section");
            assertTrue(output.contains("MOSTRANDO HISTORIAL"), "Should contain history section");
            assertTrue(output.contains("DEMOSTRACIÓN DE DESHACER/REHACER"), "Should contain undo/redo section");
            assertTrue(output.contains("ESTADO FINAL DE CUENTAS"), "Should contain final state section");
            
            // Verify specific command execution
            assertTrue(output.contains("Ejecutando comando:"), "Should show command execution");
            assertTrue(output.contains("Comando ejecutado exitosamente") || output.contains("Error:"), 
                      "Should show successful execution or errors");
            
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCommandDemoOutputContainsAccountInfo() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Execute the main method
            CommandDemo.main(new String[]{});
            
            // Capture the output
            String output = outputStream.toString();
            
            // Verify account information is displayed
            assertTrue(output.contains("Cuentas creadas:"), "Should show accounts creation");
            assertTrue(output.contains("123456789"), "Should show account number 1");
            assertTrue(output.contains("987654321"), "Should show account number 2");
            assertTrue(output.contains("Ahorros"), "Should show account type 1");
            assertTrue(output.contains("Corriente"), "Should show account type 2");
            assertTrue(output.contains("Saldo:"), "Should show account balances");
            
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCommandDemoShowsUndoRedoFunctionality() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Execute the main method
            CommandDemo.main(new String[]{});
            
            // Capture the output
            String output = outputStream.toString();
            
            // Verify undo/redo functionality is demonstrated
            assertTrue(output.contains("Deshaciendo comando:"), "Should show undo operations");
            assertTrue(output.contains("deshecho"), "Should show operation reversion");
            assertTrue(output.contains("Estado después de deshacer:"), "Should show state after undo");
            assertTrue(output.contains("Estado después de rehacer:"), "Should show state after redo");
            
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCommandDemoShowsCommandHistory() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            // Execute the main method
            CommandDemo.main(new String[]{});
            
            // Capture the output
            String output = outputStream.toString();
            
            // Verify command history is displayed
            assertTrue(output.contains("HISTORIAL FINAL"), "Should show final history");
            assertTrue(output.contains("Depósito") || output.contains("Retiro") || output.contains("Transferencia"), 
                      "Should show various command types in history");
            
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCommandDemoCompletesWithoutExceptions() {
        // This test verifies that the demo runs to completion without throwing exceptions
        try {
            CommandDemo.main(new String[]{});
            // If we reach here, the demo completed successfully
            assertTrue(true, "Demo should complete without exceptions");
        } catch (Exception e) {
            // If any exception is thrown, the test should fail
            throw new AssertionError("CommandDemo should not throw exceptions", e);
        }
    }
}