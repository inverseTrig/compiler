package symboltable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import scanner.Token;
import scanner.TokenType;

class SymbolTableTest {

	@Test
	void testGetKindA() {
		SymbolTable st = new SymbolTable();
		st.add("foo", Kind.PROCEDURE);
		Kind expectedKind = Kind.PROCEDURE;
		
		Kind returnedKind = st.getKind("foo");
		assertEquals(expectedKind, returnedKind);
	}
	
	@Test
	void testGetKindB() {
		SymbolTable st = new SymbolTable();
		st.add("foo", Kind.PROCEDURE);
		st.add("bar", Kind.FUNCTION);
		Kind expectedKind = Kind.FUNCTION;
		
		Kind returnedKind = st.getKind("bar");
		assertEquals(expectedKind, returnedKind);
	}
	
	@Test
	void testGetKindC() {
		SymbolTable st = new SymbolTable();
		st.add("foo", Kind.PROCEDURE);
		st.add("bar", Kind.FUNCTION);
		Kind expectedKind = null;
		
		Kind returnedKind = st.getKind("foobar");
		assertEquals(expectedKind, returnedKind);
	}

}
