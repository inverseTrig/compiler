package scanner;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;

class ScannerTest {
	
	// Reading in input.txt file for test cases.
	@Test
	void testNextTokenYytext() throws IOException {
		String filename = System.getProperty("user.dir") + "/input.txt";
		FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (Exception e) { e.printStackTrace(); }
        InputStreamReader isr = new InputStreamReader(fis);
        Scanner scanner = new Scanner(isr);
        
		assertEquals(new Token("program", TokenType.PROGRAM), scanner.nextToken());
		assertEquals("program", scanner.yytext());
		
		assertEquals(new Token("foo", TokenType.ID), scanner.nextToken());
		assertEquals("foo", scanner.yytext());
		
		assertEquals(new Token(";", TokenType.SEMICOLON), scanner.nextToken());
		assertEquals(";", scanner.yytext());
		
		assertEquals(new Token("var", TokenType.VAR), scanner.nextToken());
		assertEquals("var", scanner.yytext());
	}

}
