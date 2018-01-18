package scanner;

public class Token {
    public String lexeme;
    public TokenType type;
    
    public Token( String l, TokenType t) {
        this.lexeme = l;
        this.type = t;
    }
    
    public String getLexeme() {
		return this.lexeme;
	}
    
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
    
    
}
