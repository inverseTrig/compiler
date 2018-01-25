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

	}
	
	public void standard_type() {

	}
	
	public void subprogram_declarations() {

	}
	
	public void subprogram_declaration() {

	}
	
	public void subprogram_head() {

	}
	
	public void arguments() {
		
	}
	
	public void parameter_list() {
		
	}
	
	public void compound_statement() {
		
	}
	
	public void optional_statements() {
		
	}
	
	public void statement_list() {
		
	}
	
	public void statement() {
		
	}
	
	public void variable() {
		
	}
	
	public void procedure_statement() {
		
	}
	
	public void expression_list() {
		
	}
	
	public void expression() {
		
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
