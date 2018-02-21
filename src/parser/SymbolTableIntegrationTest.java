package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import symboltable.Kind;
import symboltable.SymbolTable;

class SymbolTableIntegrationTest {

	String var = "fum := 3 - 2";
	
	@Test
	void testStatementA() {
		Parser parserA = new Parser(var, false);
		parserA.getSymbolTable().add("fum", Kind.VARIABLE);
		parserA.statement();
		System.out.println(parserA.getSymbolTable());
	}

	@Test
	void testStatementB() {
		Parser parserB = new Parser(var, false);
		parserB.getSymbolTable().add("fum", Kind.PROCEDURE);
		System.out.println(parserB.getSymbolTable());
		parserB.statement();
	}
}
