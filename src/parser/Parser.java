package parser;
import java.io.IOException;

import scanner.*;

/**
 * @author heechan
 * The grammar here is based off of the Production Rules sheet we received from our professor.
 */
public class Parser {
	
	private Token lookAhead;
	private Scanner scanner;
	
	/**
	 * program id;
	 * declarations
	 * subprogram_declarations
	 * compound_statement
	 * .
	 */
	public void program() {
		match(TokenType.PROGRAM);
		match(TokenType.ID);
		match(TokenType.SEMICOLON);
		declarations();
		subprogram_declarations();
		compound_statement();
		match(TokenType.PERIOD);
	}
	
	/**
	 * id |
	 * id , idenfitier_list
	 */
	public void identifier_list() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			identifier_list();
		}
	}
	
	/**
	 * var identifier_list : type ; declarations |
	 * lambda
	 */
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
	
	/**
	 * standard_type |
	 * array [ num : num ] of standard_type
	 */
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
	
	/**
	 * integer|
	 * real
	 */
	public void standard_type() {
		if (lookAhead.getType() == TokenType.INTEGER) {
			match(TokenType.INTEGER);
		}
		else if (lookAhead.getType() == TokenType.REAL) {
			match(TokenType.REAL);
		}
	}
	
	/**
	 * subprogram_declaration ;
	 * subprogram_declarations |
	 * lambda
	 */
	public void subprogram_declarations() {
		if (lookAhead.getType() == TokenType.FUNCTION || lookAhead.getType() == TokenType.PROCEDURE) {
			subprogram_declaration();
			match(TokenType.SEMICOLON);
			subprogram_declarations();
		}
	}
	
	/**
	 * subprogram_head
	 * declarations
	 * subprogram_declarations
	 * compound_statement
	 */
	public void subprogram_declaration() {
		subprogram_head();
		declarations();
		subprogram_declarations();
		compound_statement();
	}
	
	/**
	 * function id arguments : standard_type ; |
	 * procedure id arguments ;
	 */
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
	
	/**
	 * ( parameter_list ) |
	 * lambda
	 */
	public void arguments() {
		if (lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			parameter_list();
			match(TokenType.RPARENTHESES);
		}
	}
	
	/**
	 * identifier_list : type |
	 * idenfiter_list : type ; parameter_list
	 */
	public void parameter_list() {
		identifier_list();
		match(TokenType.COLON);
		type();
		if (lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			parameter_list();
		}
	}
	
	/**
	 * begin optional_statements end
	 */
	public void compound_statement() {
		match(TokenType.BEGIN);
		optional_statements();
		match(TokenType.END);
	}
	
	/**
	 * statement_list |
	 * lambda
	 */
	public void optional_statements() {
		if (lookAhead.getType() == TokenType.ID || lookAhead.getType() == TokenType.BEGIN ||
				lookAhead.getType() == TokenType.IF || lookAhead.getType() == TokenType.WHILE) {
			statement_list();
		}
	}
	
	/**
	 * statement |
	 * statement ; statement_list
	 */
	public void statement_list() {
		statement();
		if (lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			statement_list();
		}
	}
	
	/**
	 * variable assignop expression |						// Currently only this is implemented
	 * procedure_statement |								// Utilize symbol table to implement this
	 * compound_statement |									// if (BEGIN)
	 * if expression then statement else statement |		// if (IF)
	 * while expression do statement |						// if (WHILE)
	 * read ( id ) |										// this hasn't been implemented
	 * write ( expression )									// this hasn't been implemented
	 */
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
	
	/**
	 * id |
	 * id [ expression ]
	 */
	public void variable() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.LSQBRACKET) {
			match(TokenType.LSQBRACKET);
			expression();
			match(TokenType.RSQBRACKET);
		}
	}
	
	/**
	 * id |
	 * id ( expression_list )
	 */
	public void procedure_statement() {
		match(TokenType.ID);
		if (lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			expression_list();
			match(TokenType.RPARENTHESES);
		}
	}
	
	/**
	 * expression |
	 * expression , expression_list
	 */
	public void expression_list() {
		expression();
		if (lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			expression_list();
		}
	}
	
	/**
	 * simple_expression |
	 * simple_expression relop simple_expression
	 */
	public void expression() {
		simple_expression();
		if (isRelop(lookAhead)) {
			relop();
			simple_expression();
		}
	}
	
	/**
	 * term simple_part |
	 * sign term simple_part
	 */
	public void simple_expression() {
		if (lookAhead.getType() == TokenType.MINUS || lookAhead.getType() == TokenType.PLUS) {
			sign();
		}
		term();
		simple_part();
	}
	
	/**
	 * addop term simple_part |
	 * lambda
	 */
	public void simple_part() {
		if (isAddop(lookAhead)) {
			addop();
			term();
			simple_part();
		}
	}
	
	/**
	 * factor term_part
	 */
	public void term() {
		factor();
		term_part();
	}
	
	/**
	 * mulop factor term_part |
	 * lambda
	 */
	public void term_part() {
		if (isMulop(lookAhead)) {
			mulop();
			factor();
			term_part();
		}
	}
	
	/**
	 * id |
	 * id [ expression ] |
	 * id ( expression_list ) |
	 * num |
	 * ( expression ) |
	 * not factor
	 */
	public void factor() {
		// id
		if (lookAhead.getType() == TokenType.ID) {
			match(TokenType.ID);
			if (lookAhead.getType() == TokenType.LSQBRACKET) {
				match(TokenType.LSQBRACKET);
				expression();
				match(TokenType.RSQBRACKET);
			}
			else if (lookAhead.getType() == TokenType.LPARENTHESES) {
				match(TokenType.LPARENTHESES);
				expression_list();
				match(TokenType.RPARENTHESES);
			}
		}
		else if (lookAhead.getType() == TokenType.NUMBER) {
			match(TokenType.NUMBER);
		}
		else if (lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			expression();
			match(TokenType.RPARENTHESES);
		}
		else if (lookAhead.getType() == TokenType.NOT) {
			match(TokenType.NOT);
			factor();
		}
		else {
			error("factor()");
		}
	}
	
	/**
	 * + |
	 * -
	 */
	public void sign() {
		if (lookAhead.getType() == TokenType.PLUS) {
			match(TokenType.PLUS);
		}
		else if (lookAhead.getType() == TokenType.MINUS) {
			match(TokenType.MINUS);
		}
		else {
			error("sign()");
		}
	}
	
	/**
	 * Checks if the token that is passed in is a +, -, or OR.
	 * @param token
	 * @return boolean true or false based on the result
	 */
	public boolean isAddop(Token token) {
		TokenType tokentype = token.getType();
		if (tokentype == TokenType.PLUS || tokentype == TokenType.MINUS || 
				tokentype == TokenType.OR) {
			return true;
		}
		return false;
	}
	
	/**
	 * Matches one of the addops specified above, +, -, or OR.
	 */
	public void addop() {
		switch (lookAhead.getType()) {
		case PLUS:
			match(TokenType.PLUS);
			break;
		case MINUS:
			match(TokenType.MINUS);
			break;
		case OR:
			match(TokenType.OR);
			break;
		default:
			error("addop()");
			break;
		}
	}
	
	/**
	 * Checks if the token that is passed in is a *, /, DIV, MOD, or AND.
	 * @param token
	 * @return boolean true or false based on the result
	 */
	public boolean isMulop(Token token) {
		TokenType tokentype = token.getType();
		if (tokentype == TokenType.ASTERISK || tokentype == TokenType.SLASH || 
				tokentype == TokenType.DIV || tokentype == TokenType.MOD || 
				tokentype == TokenType.AND) {
			return true;
		}
		return false;
	}
	
	/**
	 * Matches one of the mulops specified above, *, /, DIV, MOD, or AND.
	 */
	public void mulop() {
		switch (lookAhead.getType()) {
		case ASTERISK:
			match(TokenType.ASTERISK);
			break;
		case SLASH:
			match(TokenType.SLASH);
			break;
		case DIV:
			match(TokenType.DIV);
			break;
		case MOD:
			match(TokenType.MOD);
			break;
		case AND:
			match(TokenType.AND);
			break;
		default:
			error("mulop()");
			break;
		}
	}
	
	/**
	 * Checks if the token that is passed in is a =, <>, <, <=, >=, or >.
	 * @param token
	 * @return boolean true or false based on the result
	 */
	public boolean isRelop(Token token) {
		TokenType tokentype = token.getType();
		if (tokentype == TokenType.EQUAL || tokentype == TokenType.NOTEQUAL || 
				tokentype == TokenType.LESSTHAN || tokentype == TokenType.LESSTHANOREQUALTO || 
				tokentype == TokenType.GREATERTHAN || tokentype == TokenType.GREATERTHANOREQUALTO) {
			return true;
		}
		return false;
	}
	
	/**
	 * Matches one of the mulops specified above, =, <>, <, <=, >=, or >.
	 */
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
			error("relop()");
			break;
		}
	}
	
	/**
	 * This method 
	 * @param expected
	 */
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
