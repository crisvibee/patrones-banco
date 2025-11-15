package com.example.unit.Observer;

import com.example.Observer.view.ObserverDemo;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverDemoUnitTest {

    @Test
    public void testObserverDemoExecutionWithoutErrors() {
  
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
    
            ObserverDemo.main(new String[]{});
            
            String output = outContent.toString();
            assertNotNull(output);
            assertFalse(output.isEmpty());
            
        } finally {
       
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoOutputContainsExpectedMessages() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.contains("NOTIFICACION OBSERVER"));
            assertTrue(output.contains("OPERACIONES BANCARIAS"));
            assertTrue(output.contains("ESTADO FINAL"));
            assertTrue(output.contains("Observadores registrados"));
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoContainsDepositOperation() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.contains("depósito") || output.contains("Depósito"));
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoContainsWithdrawOperation() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.contains("retiro") || output.contains("Retiro"));
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoContainsTransferOperation() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.contains("transferencia") || output.contains("Transferencia"));
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoShowsFinalState() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.contains("ESTADO FINAL"));
            assertTrue(output.contains("Observadores registrados"));
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testObserverDemoOutputFormat() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            ObserverDemo.main(new String[]{});
            String output = outContent.toString();

            assertTrue(output.length() > 100, "Output should contain meaningful content");
            assertTrue(output.split("\\n").length >= 10, "Output should have multiple lines");
            
        } finally {
            System.setOut(originalOut);
        }
    }
}