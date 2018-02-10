package symboltable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import scanner.Token;
import scanner.TokenType;

class SymbolTableTest {

	/**
	 * Testing adding a procedure name into our SymbolTable.
	 * Here, we can see that the returned kind is what we expected it to be.
	 */
	@Test
	void testGetKindA() {
		SymbolTable st = new SymbolTable();
		st.add("foo", Kind.PROCEDURE);
		Kind expectedKind = Kind.PROCEDURE;
		
		Kind returnedKind = st.getKind("foo");
		assertEquals(expectedKind, returnedKind);
	}
	
	/**
	 * Testing adding two different things into our SymbolTable
	 * and seeing if the returned kind that we are looking for matches what we entered.
	 */
	@Test
	void testGetKindB() {
		SymbolTable st = new SymbolTable();
		st.add("foo", Kind.PROCEDURE);
		st.add("bar", Kind.FUNCTION);
		Kind expectedKind = Kind.FUNCTION;
		
		Kind returnedKind = st.getKind("bar");
		assertEquals(expectedKind, returnedKind);
	}
	
	/**
	 * Here, we see that "foobar" is not inside our SymbolTable.
	 * As it is not in our SymbolTable, it should return null, and we see that this works as intended as well.
	 */
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
