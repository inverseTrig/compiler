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
        
        Token expectedToken = new Token("program", TokenType.PROGRAM);
        Token returnedToken = scanner.nextToken();
        assertEquals(expectedToken, returnedToken);
		assertEquals("program", scanner.yytext());
		
		expectedToken = new Token("foo", TokenType.ID);
        returnedToken = scanner.nextToken();
        assertEquals(expectedToken, returnedToken);
		assertEquals("foo", scanner.yytext());
		
		expectedToken = new Token(";", TokenType.SEMICOLON);
        returnedToken = scanner.nextToken();
        assertEquals(expectedToken, returnedToken);
		assertEquals(";", scanner.yytext());
		
		expectedToken = new Token("var", TokenType.VAR);
        returnedToken = scanner.nextToken();
        assertEquals(expectedToken, returnedToken);
		assertEquals("var", scanner.yytext());
		
	}

}
