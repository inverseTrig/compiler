package scanner;

/**
 * @author heechan
 * Token class is where we define a new object called Token. A Token will hold two values - a String lexeme, and a TokenType type.
 * The Scanner reads in a lexeme, uses the LookupTable to find if the lexeme exists, and if so, matches it to its appropriate TokenType.
 * Then a Token object is created, with its lexeme and the appropriate type.
 */
public class Token {
    public String lexeme;
    public TokenType type;
    
    /**
     * Constructor for a Token. A Token MUST always have a String lexeme and a TokenType type. Thus it will not have an empty constructor.
     * @param l String lexeme read in via the Scanner.
     * @param t TokenType type is matched using the LookupTable.
     */
    public Token( String l, TokenType t) {
        this.lexeme = l;
        this.type = t;
    }

    /**
     * Override the toString() method, as it originally prints out hex codes.
     * The if statement is there for readability when printed out, as I use tabbing, this makes it such that everything is correctly aligned for ease of view.
     */
    @Override
    public String toString() { 
    	if (this.type == TokenType.LESSTHANOREQUALTO ||
    			this.type == TokenType.GREATERTHANOREQUALTO ||
    			this.type == TokenType.LSQBRACKET ||
				this.type == TokenType.RSQBRACKET ||
				this.type == TokenType.GREATERTHAN ||
    			this.type == TokenType.LPARENTHESES ||
    			this.type == TokenType.RPARENTHESES) {
    		return "Token: " + this.type + "\t| Lexeme: " + this.lexeme;
    	}
    	return "Token: " + this.type + "\t\t| Lexeme: " + this.lexeme;
    }


    /**
     * Override equals() method for JUnit testing to run appropriately.
     * Comparing objects in JUnit without overriding equals() will return false unless they are literally the same object.
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (lexeme == null) {
			if (other.lexeme != null)
				return false;
		} else if (!lexeme.equals(other.lexeme))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

    public String getLexeme() {
		return this.lexeme;
	}
    
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
    
    
}
