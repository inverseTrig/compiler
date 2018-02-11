package symboltable;

/**
 * Defining the ENUMs of Kind that we will be using when creating DataStorage in SymbolTable.
 * These ENUMs will also be used by the semantic analyzer as well.
 */
public enum Kind {
	PROGRAM,
	VARIABLE,
	FUNCTION,
	PROCEDURE, //Not sure if this one is included.
	ARRAY,
}
