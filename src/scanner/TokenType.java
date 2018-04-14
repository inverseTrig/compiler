package scanner;

/**
 * Defining the ENUMs of TokenType that we will be using when creating Tokens.
 * These ENUMs will also be used in the LookupTable to pair with the appropriate lexeme.
 */
public enum TokenType {
	ID, ILLEGAL, WRITE, READ,
    AND, ARRAY, BEGIN, DIV, DO, ELSE, END, FUNCTION, IF, INTEGER, MOD, NOT, OF, OR,
    PROCEDURE, PROGRAM, REAL, THEN, VAR, WHILE, SEMICOLON, COMMA, PERIOD, COLON,
    LSQBRACKET, RSQBRACKET, LPARENTHESES, RPARENTHESES, PLUS, MINUS, EQUAL, NOTEQUAL,
    LESSTHAN, LESSTHANOREQUALTO, GREATERTHAN, GREATERTHANOREQUALTO, ASTERISK, SLASH, BECOMES,
}