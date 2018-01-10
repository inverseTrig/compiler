package scanner;

import java.util.HashMap;

@SuppressWarnings("serial")
public class LookupTable extends HashMap<String, TokenType> {
	
	public LookupTable() {
		this.put("and", TokenType.AND);
		this.put("array", TokenType.ARRAY);
		this.put("begin", TokenType.BEGIN);
		this.put("div", TokenType.DIV);
		this.put("do", TokenType.DO);
		this.put("else", TokenType.ELSE);
		this.put("end", TokenType.END);
		this.put("function", TokenType.FUNCTION);
		this.put("if", TokenType.IF);
		this.put("integer", TokenType.INTEGER);
		this.put("mod", TokenType.MOD);
		this.put("not", TokenType.NOT);
		this.put("of", TokenType.OF);
		this.put("or", TokenType.OR);
		this.put("procedure", TokenType.PROCEDURE);
		this.put("program", TokenType.PROGRAM);
		this.put("real", TokenType.REAL);
		this.put("then", TokenType.THEN);
		this.put("var", TokenType.VAR);
		this.put("while", TokenType.WHILE);
		this.put(";", TokenType.SEMICOLON);
		this.put(",", TokenType.COMMA);
		this.put(".", TokenType.PERIOD);
		this.put("[", TokenType.LSQBRACKET);
		this.put("]", TokenType.RSQBRACKET);
		this.put("(", TokenType.LPARENTHESES);
		this.put(")", TokenType.RPARENTHESES);
		this.put("+", TokenType.PLUS);
		this.put("-", TokenType.MINUS);
		this.put("=", TokenType.EQUAL);
		this.put("<>", TokenType.NOTEQUAL);
		this.put("<", TokenType.LESSTHAN);
		this.put("<=", TokenType.LESSTHANOREQUALTO);
		this.put(">", TokenType.GREATERTHAN);
		this.put(">=", TokenType.GREATERTHANOREQUALTO);
		this.put("*", TokenType.ASTERISK);
		this.put("/", TokenType.SLASH);
		this.put(":=", TokenType.BECOMES);
	}
}

