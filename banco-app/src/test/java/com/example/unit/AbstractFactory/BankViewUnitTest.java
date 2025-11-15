package com.example.unit.AbstractFactory;

import com.example.AbstractFactory.view.BankView;
import com.example.AbstractFactory.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class BankViewUnitTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    
        BankView.nAccounts = 1;
        BankView.nCards = 1;
        BankView.nCreditLines = 1;
    }

    @Test
    void testMainWithRetailBankChoice() {
       
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

     
        BankView.main(new String[]{});

      
        String output = outContent.toString();
        assertTrue(output.contains("¿Desea abrir Savings Account (1) o Checking Account (2)?"));
        assertTrue(output.contains("Checking account opened") || output.contains("Savings account opened"));
        assertTrue(output.contains("Debit card activated") || output.contains("debit card activated"));
        assertTrue(output.contains("Personal credit line approved") || output.contains("personal credit line approved"));
    }

    @Test
    void testMainWithCorporateBankChoice() {
     
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

     
        BankView.main(new String[]{});

      
        String output = outContent.toString();
        assertTrue(output.contains("¿Desea abrir Savings Account (1) o Checking Account (2)?"));
        assertTrue(output.contains("Savings account opened") || output.contains("Checking account opened"));
        assertTrue(output.contains("Credit card activated") || output.contains("credit card activated"));
        assertTrue(output.contains("Business credit line approved") || output.contains("business credit line approved"));
    }

    @Test
    void testMainWithInvalidChoice() {
     
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

   
        BankView.main(new String[]{});

       
        String output = outContent.toString();
        assertTrue(output.contains("¿Desea abrir Savings Account (1) o Checking Account (2)?"));
        assertTrue(output.contains("Elección inválida"));
    
        assertFalse(output.contains("account opened"));
        assertFalse(output.contains("card activated"));
        assertFalse(output.contains("credit line approved"));
    }

    @Test
    void testMainWithInvalidInput() {
        
        String input = "abc\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BankView.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("¿Desea abrir Savings Account (1) o Checking Account (2)?"));
        assertTrue(output.contains("Elección inválida") || output.contains("inválida"));
    }

    @Test
    void testMainWithMultipleProducts() {
   
        BankView.nAccounts = 2;
        BankView.nCards = 3;
        BankView.nCreditLines = 1;

        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BankView.main(new String[]{});

        String output = outContent.toString();
   
        long accountOpenedCount = output.toLowerCase().lines()
                .filter(line -> line.contains("account opened"))
                .count();
        long cardActivatedCount = output.toLowerCase().lines()
                .filter(line -> line.contains("card activated") || line.contains("activated"))
                .count();

        assertEquals(2, accountOpenedCount, "Deberían crearse 2 cuentas");
        assertEquals(3, cardActivatedCount, "Deberían activarse 3 tarjetas");
    }

    @Test
    void testMainWithZeroProducts() {
     
        BankView.nAccounts = 0;
        BankView.nCards = 0;
        BankView.nCreditLines = 0;

      
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        BankView.main(new String[]{});

 
        String output = outContent.toString();
        assertTrue(output.contains("¿Desea abrir Savings Account (1) o Checking Account (2)?"));
   
        assertFalse(output.contains("account opened"));
        assertFalse(output.contains("card activated"));
        assertFalse(output.contains("credit line approved"));
    }

    @Test
    void testStaticVariablesAccess() {
       
        BankView.nAccounts = 5;
        BankView.nCards = 2;
        BankView.nCreditLines = 3;

        assertEquals(5, BankView.nAccounts);
        assertEquals(2, BankView.nCards);
        assertEquals(3, BankView.nCreditLines);
    }
}