package parser;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import scanner.*;
import syntaxtree.*;
import symboltable.Kind;
import symboltable.SymbolTable;


/**
 * @author heechan
 * The grammar here is based off of the Production Rules sheet we received from our professor.
 */
public class Parser {
	
	private Token lookAhead;
	private Scanner scanner;
	private SymbolTable symTab;
	
	/**
	 * Constructor for the Parser. Here, we pass in a string and a boolean.
	 * The boolean indicates if the string is a file name or a string.
	 * @param s String that is passed in.
	 * @param isFile boolean that indicates if the String is a file name or not.
	 * @throws IOException 
	 */
	public Parser(String s, boolean isFile) {
		symTab = new SymbolTable();
		if (isFile) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(s);
			} catch (Exception e) { e.printStackTrace(); }
			InputStreamReader isr = new InputStreamReader(fis);
	        scanner = new Scanner(isr);
		}
		else {
			scanner = new Scanner(new StringReader(s));
		}
		
		try {
			lookAhead = scanner.nextToken();
		} catch (IOException e) {error("Scan error"); }
	}
	
	/**
	 * program id;
	 * declarations
	 * subprogram_declarations
	 * compound_statement
	 * .
	 */
	public ProgramNode program() {		
		match(TokenType.PROGRAM);
		
		ProgramNode pNode = new ProgramNode(lookAhead.getLexeme());
		
		this.symTab.add(lookAhead.getLexeme(), Kind.PROGRAM);
		match(TokenType.ID);
		match(TokenType.SEMICOLON);
		pNode.setVariables(declarations(new DeclarationsNode()));
		pNode.setFunctions(subprogram_declarations(new SubProgramDeclarationsNode()));
		pNode.setMain(compound_statement());
		match(TokenType.PERIOD);
		
		return pNode;
	}
	
	/**
	 * id |
	 * id , idenfitier_list
	 */
	public ArrayList<VariableNode> identifier_list(ArrayList<VariableNode> vNodes) {
		vNodes.add(new VariableNode(lookAhead.getLexeme()));
		this.symTab.add(lookAhead.getLexeme(), Kind.VARIABLE);
		
		match(TokenType.ID);
		if (lookAhead != null && lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			vNodes = identifier_list(vNodes);
		}
		
		return vNodes;
	}
	
	/**
	 * var identifier_list : type ; declarations |
	 * lambda
	 */
	public DeclarationsNode declarations(DeclarationsNode decNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<>();
		
		if (lookAhead != null && lookAhead.getType() == TokenType.VAR) {
			match(TokenType.VAR);
			vNodes = identifier_list(new ArrayList<VariableNode>());
			match(TokenType.COLON);
			type();
			match(TokenType.SEMICOLON);
			decNode = declarations(decNode);
		}
		
		for (VariableNode vN : vNodes) {
			decNode.addVariable(vN);
		}
		
		return decNode;
	}
	
	/**
	 * standard_type |
	 * array [ num : num ] of standard_type
	 */
	public void type() {
		if (lookAhead != null && lookAhead.getType() == TokenType.ARRAY) {
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
		if (lookAhead != null && lookAhead.getType() == TokenType.INTEGER) {
			match(TokenType.INTEGER);
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.REAL) {
			match(TokenType.REAL);
		}
		else {
			error("standard_type()");
		}
	}
	
	/**
	 * subprogram_declaration ;
	 * subprogram_declarations |
	 * lambda
	 */
	public SubProgramDeclarationsNode subprogram_declarations(SubProgramDeclarationsNode subProgDecNode) {
		
		if (lookAhead != null && (lookAhead.getType() == TokenType.FUNCTION || lookAhead.getType() == TokenType.PROCEDURE)) {
			subProgDecNode.addSubProgramDeclaration(subprogram_declaration());
			match(TokenType.SEMICOLON);
			subProgDecNode = subprogram_declarations(subProgDecNode);
		}
		
		return subProgDecNode;
	}
	
	/**
	 * subprogram_head
	 * declarations
	 * subprogram_declarations
	 * compound_statement
	 */
	public SubProgramNode subprogram_declaration() {
		SubProgramNode subProgNode = subprogram_head();
		subProgNode.setVariables(declarations(new DeclarationsNode()));
		subProgNode.setFunctions(subprogram_declarations(new SubProgramDeclarationsNode()));
		subProgNode.setMain(compound_statement());
		
		return subProgNode;
	}
	
	/**
	 * function id arguments : standard_type ; |
	 * procedure id arguments ;
	 */
	public SubProgramNode subprogram_head() {
		SubProgramNode subProgNode = null;
		
		if (lookAhead != null && lookAhead.getType() == TokenType.FUNCTION) {
			match(TokenType.FUNCTION);
			subProgNode = new SubProgramNode(lookAhead.getLexeme());
			this.symTab.add(lookAhead.getLexeme(), Kind.FUNCTION);
			match(TokenType.ID);
			
			ArrayList<VariableNode> vNodes = arguments(new ArrayList<VariableNode>());
			ParameterStatementNode paramNode = new ParameterStatementNode();
			for (VariableNode vN : vNodes) {
				paramNode.addVariable(vN);
			}
			subProgNode.setParameters((paramNode));
			
			match(TokenType.COLON);
			standard_type();
			match(TokenType.SEMICOLON);
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.PROCEDURE) {
			match(TokenType.PROCEDURE);
			subProgNode = new SubProgramNode(lookAhead.getLexeme());
			subProgNode.setType(SubProgramType.PROCEDURE);
			this.symTab.add(lookAhead.getLexeme(), Kind.PROCEDURE);
			match(TokenType.ID);
			ArrayList<VariableNode> vNodes = arguments(new ArrayList<VariableNode>());
			ParameterStatementNode paramNode = new ParameterStatementNode();
			for (VariableNode vN : vNodes) {
				paramNode.addVariable(vN);
			}
			subProgNode.setParameters(paramNode);
			System.out.println(subProgNode.indentedToString(0));
			
			match(TokenType.SEMICOLON);
		}
		else {
			error("subprogram_head()");
		}
		
		return subProgNode;
	}
	
	/**
	 * ( parameter_list ) |
	 * lambda
	 */
	public ArrayList<VariableNode> arguments(ArrayList<VariableNode> vNodes) {
		
		if (lookAhead != null && lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			vNodes = parameter_list(vNodes);
			match(TokenType.RPARENTHESES);
		}
		
		return vNodes;
	}
	
	/**
	 * identifier_list : type |
	 * idenfiter_list : type ; parameter_list
	 */
	public ArrayList<VariableNode> parameter_list(ArrayList<VariableNode> vNodes) {
		ArrayList<VariableNode> vNode = identifier_list(new ArrayList<VariableNode>());
		match(TokenType.COLON);
		type();
		if (lookAhead != null && lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			vNode = parameter_list(vNode);
		}
		
		for (VariableNode vN : vNode) {
			vNodes.add(vN);
		}
		
		return vNodes;
	}
	
	/**
	 * begin optional_statements end
	 */
	public CompoundStatementNode compound_statement() {
		CompoundStatementNode compStatNode = new CompoundStatementNode();
		
		match(TokenType.BEGIN);
		compStatNode = optional_statements(compStatNode);
		match(TokenType.END);
		
		return compStatNode;
	}
	
	/**
	 * statement_list |
	 * lambda
	 */
	public CompoundStatementNode optional_statements(CompoundStatementNode compStatNode) {
		if (lookAhead != null && (lookAhead.getType() == TokenType.ID || lookAhead.getType() == TokenType.BEGIN ||
				lookAhead.getType() == TokenType.IF || lookAhead.getType() == TokenType.WHILE)) {
			compStatNode = statement_list(compStatNode);
		}
		
		return compStatNode;
	}
	
	/**
	 * statement |
	 * statement ; statement_list
	 */
	public CompoundStatementNode statement_list(CompoundStatementNode compStatNode) {
		compStatNode.addStatement(statement());
		if (lookAhead != null && lookAhead.getType() == TokenType.SEMICOLON) {
			match(TokenType.SEMICOLON);
			compStatNode = statement_list(compStatNode);
		}
		return compStatNode;
	}
	
	/**
	 * variable assignop expression |						// if kind.variable
	 * procedure_statement |								// if kind.procedure
	 * compound_statement |									// if (BEGIN)
	 * if expression then statement else statement |		// if (IF)
	 * while expression do statement |						// if (WHILE)
	 * read ( id ) |										// this hasn't been implemented
	 * write ( expression )									// this hasn't been implemented
	 */
	public StatementNode statement() {
		if (lookAhead != null && lookAhead.getType() == TokenType.ID) {
			if (this.symTab.isKind(lookAhead.getLexeme(), Kind.VARIABLE)) {
				AssignmentStatementNode assignStatNode = new AssignmentStatementNode();
				assignStatNode.setLvalue(variable());
				match(TokenType.BECOMES);
				assignStatNode.setExpression(expression());
				return assignStatNode;
			}
			else if (this.symTab.isKind(lookAhead.getLexeme(), Kind.PROCEDURE)) {
				procedure_statement();
			}
			else {
				error("statement().VARorPROC");
			}
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.BEGIN) {
			compound_statement();
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.IF) {
			match(TokenType.IF);
			expression();
			match(TokenType.THEN);
			statement();
			match(TokenType.ELSE);
			statement();
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.WHILE) {
			match(TokenType.WHILE);
			expression();
			match(TokenType.DO);
			statement();
		}
		else {
			error("statement()");
		}
		return null;
	}
	
	/**
	 * id |
	 * id [ expression ]
	 */
	public VariableNode variable() {
		VariableNode vNode = new VariableNode(lookAhead.getLexeme());
		match(TokenType.ID);
		if (lookAhead != null && lookAhead.getType() == TokenType.LSQBRACKET) {
			match(TokenType.LSQBRACKET);
			expression();
			match(TokenType.RSQBRACKET);
		}
		return vNode;
	}
	
	/**
	 * id |
	 * id ( expression_list )
	 */
	public void procedure_statement() {
		match(TokenType.ID);
		if (lookAhead != null && lookAhead.getType() == TokenType.LPARENTHESES) {
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
		if (lookAhead != null && lookAhead.getType() == TokenType.COMMA) {
			match(TokenType.COMMA);
			expression_list();
		}
	}
	
	/**
	 * simple_expression |
	 * simple_expression relop simple_expression
	 */
	public ExpressionNode expression() {
		ExpressionNode eNode = simple_expression();
		if (lookAhead != null && isRelop(lookAhead)) {
			relop();
			simple_expression();
		}
		return eNode;
	}
	
	/**
	 * term simple_part |
	 * sign term simple_part
	 */
	public ExpressionNode simple_expression() {
		ExpressionNode eNode = null;
		if (lookAhead != null && (lookAhead.getType() == TokenType.MINUS || lookAhead.getType() == TokenType.PLUS)) {
			eNode = sign();
		}
		eNode = term();
		simple_part();
		
		return eNode;
	}
	
	/**
	 * addop term simple_part |
	 * lambda
	 */
	public OperationNode simple_part() {
		OperationNode oNode = null;
		if (lookAhead != null && isAddop(lookAhead)) {
			oNode.setOperation(addop());
			term();
			simple_part();
		}
		return oNode;
	}
	
	/**
	 * factor term_part
	 */
	public ExpressionNode term() {
		ExpressionNode eNode = null;
		eNode = factor();
		eNode = term_part(eNode);
		return eNode;
	}
	
	/**
	 * mulop factor term_part |
	 * lambda
	 */
	public ExpressionNode term_part(ExpressionNode eNode) {
		OperationNode oNode = null;
		if (lookAhead != null && isMulop(lookAhead)) {
			oNode = new OperationNode(mulop());
			oNode.setLeft(eNode);
			oNode.setRight(factor());
			return term_part(oNode);
		}
		return eNode;
	}
	
	/**
	 * id |
	 * id [ expression ] |
	 * id ( expression_list ) |
	 * num |
	 * ( expression ) |
	 * not factor
	 */
	public ExpressionNode factor() {
		// id
		if (lookAhead != null && lookAhead.getType() == TokenType.ID) {
			String name = lookAhead.getLexeme();
			match(TokenType.ID);
			if (lookAhead != null && lookAhead.getType() == TokenType.LSQBRACKET) {
				match(TokenType.LSQBRACKET);
				ArrayNode eNode = new ArrayNode(name);
				eNode.setExpression(expression());
				match(TokenType.RSQBRACKET);
				return eNode;
			}
			else if (lookAhead != null && lookAhead.getType() == TokenType.LPARENTHESES) {
				match(TokenType.LPARENTHESES);
				// PROCEDURENODE
				
				expression_list();
				match(TokenType.RPARENTHESES);
				
			}
			return new VariableNode(name);
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.NUMBER) {
			ValueNode vNode = new ValueNode(lookAhead.getLexeme());
			match(TokenType.NUMBER);
			return vNode;
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.LPARENTHESES) {
			match(TokenType.LPARENTHESES);
			ExpressionNode eNode = expression();
			match(TokenType.RPARENTHESES);
			return eNode;
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.NOT) {
			match(TokenType.NOT);
			return factor();
		}
		else {
			error("factor()");
			return null;
		}
	}
	
	/**
	 * + |
	 * -
	 */
	public OperationNode sign() {
		OperationNode oNode = null;
		if (lookAhead != null && lookAhead.getType() == TokenType.PLUS) {
			match(TokenType.PLUS);
			oNode = new OperationNode(TokenType.PLUS);
			oNode.setLeft(new ValueNode("0"));
		}
		else if (lookAhead != null && lookAhead.getType() == TokenType.MINUS) {
			match(TokenType.MINUS);
			oNode = new OperationNode(TokenType.MINUS);
			oNode.setLeft(new ValueNode("0"));
		}
		else {
			error("sign()");
		}
		return oNode;
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
	public TokenType addop() {
		switch (lookAhead.getType()) {
			case PLUS:
				match(TokenType.PLUS);
				return TokenType.PLUS;
			case MINUS:
				match(TokenType.MINUS);
				return TokenType.MINUS;
			case OR:
				match(TokenType.OR);
				return TokenType.OR;
			default:
				error("addop()");
				return TokenType.ILLEGAL;
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
	public TokenType mulop() {
		switch (lookAhead.getType()) {
			case ASTERISK:
				match(TokenType.ASTERISK);
				return TokenType.ASTERISK;
			case SLASH:
				match(TokenType.SLASH);
				return TokenType.SLASH;
			case DIV:
				match(TokenType.DIV);
				return TokenType.DIV;
			case MOD:
				match(TokenType.MOD);
				return TokenType.MOD;
			case AND:
				match(TokenType.AND);
				return TokenType.AND;
			default:
				error("mulop()");
				return TokenType.ILLEGAL;
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
	 * This method matches the lexeme at the current index to the type passed in.
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
        else {
            error("Match of " + expected + " found " + this.lookAhead.getType()
                    + " instead.");
        }
	}
	
	/**
	 * This method is used in the methods above to print out an error message when something illegal happens.
	 * (Illegal in the sense that the grammar is incorrect)
	 * @param message
	 */
	public void error (String message) {
		System.out.println("Error: " + message);
	}
	
	/**
	 * This method prints out a .symboltable file into the directory of the project.
	 * @param s
	 */
	public void writeOut(String s) {
		PrintWriter write;
		try {
			write = new PrintWriter(new BufferedWriter (new FileWriter(s.substring(0, s.length() - 4) + ".symboltable")));
			write.println(this.symTab.toString());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method returns the symbol table of the parser; this method is used in the JUnit test.
	 * @return SymbolTable of the parser
	 */
	public SymbolTable getSymbolTable() {
		return this.symTab;
	}
	
}
