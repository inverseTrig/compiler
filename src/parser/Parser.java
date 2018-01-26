package parser;
import java.io.IOException;

import scanner.*;

public class Parser {
	
	private Token lookAhead;
	private Scanner scanner;
	
	
	public void program() {
		match(TokenType.PROGRAM);
		match(TokenType.ID);
		match(TokenType.SEMICOLON);
		declarations();
		subprogram_declarations();
		compound_statement();
		match(TokenType.PERIOD);
	}
	
	public void identifier_list() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			identifier_list();
		}
	}
	
	public void declarations() {
		if (lookAhead.getType() == TokenType.VAR) {
			match(TokenType.VAR);
			identifier_list();
			match(TokenType.COLON);
			type();
			match(TokenType.SEMICOLON);
			declarations();
		}
	}
	
	public void type() {
		if (lookAhead.getType() == TokenType.ARRAY) {
			match(TokenType.ARRAY);
			match(TokenType.LSQBRACKET);
			match(TokenType.NUMBER);
			match(TokenType.COLON);
			match(TokenType.NUMBER);
			match(TokenType.RSQBRACKET);
			match(TokenType.OF);
		}
		standard_type();
	}
	
	public void standard_type() {
		if (lookAhead.getType() == TokenType.INTEGER) {
			match(TokenType.INTEGER);
		}
		else if (lookAhead.getType() == TokenType.REAL) {
			match(TokenType.REAL);
		}
	}
	
	public void subprogram_declarations() {
		if (lookAhead.getType() == TokenType.FUNCTION || lookAhead.getType() == TokenType.PROCEDURE) {
			subprogram_declaration();
			match(TokenType.SEMICOLON);
			subprogram_declarations();
		}
	}
	
	public void subprogram_declaration() {
		subprogram_head();
		declarations();
		subprogram_declarations();
		compound_statement();
	}
	
	public void subprogram_head() {
		if (lookAhead.getType() == TokenType.FUNCTION) {
			match(TokenType.FUNCTION);
			match(TokenType.ID);
			arguments();
			match(TokenType.COLON);
			standard_type();
			match(TokenType.SEMICOLON);
		}
		else if (lookAhead.getType() == TokenType.PROCEDURE) {
			match(TokenType.PROCEDURE);
			match(TokenType.ID);
			arguments();
			match(TokenType.SEMICOLON);
		}
		
	}
	
	public void arguments() {
		if (lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			parameter_list();
			match(TokenType.RPARENTHESES);
		}
	}
	
	public void parameter_list() {
		identifier_list();
		match(TokenType.COLON);
		type();
		if (lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			parameter_list();
		}
	}
	
	public void compound_statement() {
		match(TokenType.BEGIN);
		optional_statements();
		match(TokenType.END);
	}
	
	public void optional_statements() {
		if (lookAhead.getType() == TokenType.ID || lookAhead.getType() == TokenType.BEGIN ||
				lookAhead.getType() == TokenType.IF || lookAhead.getType() == TokenType.WHILE) {
			statement_list();
		}
	}
	
	public void statement_list() {
		if (lookAhead.getType() == TokenType.ID || lookAhead.getType() == TokenType.BEGIN ||
				lookAhead.getType() == TokenType.IF || lookAhead.getType() == TokenType.WHILE) {
			statement();
		}
		if (lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			statement_list();
		}
	}
	
	public void statement() {
		if (lookAhead.getType() == TokenType.ID) {
			variable();
			match(TokenType.BECOMES);
			expression();
		}
		else if (lookAhead.getType() == TokenType.BEGIN) {
			compound_statement();
		}
		else if (lookAhead.getType() == TokenType.IF) {
			match(TokenType.IF);
			expression();
			match(TokenType.THEN);
			statement();
			match(TokenType.ELSE);
			statement();
		}
		else if (lookAhead.getType() == TokenType.WHILE) {
			match(TokenType.WHILE);
			expression();
			match(TokenType.DO);
			statement();
		}
	}
	
	public void variable() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.LSQBRACKET) {
			match(TokenType.LSQBRACKET);
			expression();
			match(TokenType.RSQBRACKET);
		}
	}
	
	public void procedure_statement() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			expression_list();
			match(TokenType.RPARENTHESES);
		}
	}
	
	public void expression_list() {
		expression();
		if (lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			expression_list();
		}
	}
	
	public void expression() {
		simple_expression();
		if (isRelop(lookAhead)) {
			relop();
			simple_expression();
		}
	}
	
	public void simple_expression() {
		
	}
	
	public void simple_part() {
		
	}
	
	public void term() {
		
	}
	
	public void term_part() {
		
	}
	
	public void factor() {
		
	}
	
	public void sign() {
		
	}
	
	public boolean isRelop(Token token) {
		TokenType tokentype = token.getType();
		if (tokentype == TokenType.EQUAL || tokentype == TokenType.NOTEQUAL || 
				tokentype == TokenType.LESSTHAN || tokentype == TokenType.LESSTHANOREQUALTO || 
				tokentype == TokenType.GREATERTHAN || tokentype == TokenType.GREATERTHANOREQUALTO) {
			return true;
		}
		return false;
	}
	
	public void relop() {
		switch (lookAhead.getType()) {
			case EQUAL:
				match(TokenType.EQUAL);
				break;
			case NOTEQUAL:
				match(TokenType.NOTEQUAL);
				break;
			case LESSTHAN:
				match(TokenType.LESSTHAN);
				break;
			case LESSTHANOREQUALTO:
				match(TokenType.LESSTHANOREQUALTO);
				break;
			case GREATERTHAN:
				match(TokenType.GREATERTHAN);
				break;
			case GREATERTHANOREQUALTO:
				match(TokenType.GREATERTHANOREQUALTO);
				break;
			default:
				error("relop");
				break;
		}
	}
	
	public void match(TokenType expected) {
		if (this.lookAhead.getType() == expected) {
			try {
				this.lookAhead = scanner.nextToken();
				if (this.lookAhead == null) {
					this.lookAhead = new Token("EOF", null);
				}
			} catch (IOException e) {
				error("IOException from Scanner");
			}
		}
	}
	
	public void error (String message) {
		System.out.println("Error " + message);
	}
}
