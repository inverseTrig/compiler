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
    public String toString() { return "Token: " + this.type + "\t| Lexeme: " + this.lexeme; }
}
