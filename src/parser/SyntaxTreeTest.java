package parser;

import static org.junit.jupiter.api.Assertions.*;

import syntaxtree.*;
import org.junit.jupiter.api.Test;

import scanner.TokenType;
import symboltable.Kind;

/**
 * JUnit testing for SyntaxTree integration to the Parser.
 * @author heechan
 *
 */
class SyntaxTreeTest {

	/**
	 * Creating a ProgramNode manually and compare it to the one Parser creates.
	 */
	@Test
	void testProgramNodeA() {
		ProgramNode pNode = new ProgramNode("foo");
		pNode.setVariables(new DeclarationsNode());
		pNode.setFunctions(new SubProgramDeclarationsNode());
		pNode.setMain(new CompoundStatementNode());
		
		String expected = pNode.indentedToString(0);
		
		String test = "program foo; begin end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Creating a ProgramNode manually and compare it to the one Parser creates.
	 * Different than the first one, here we add additional variables, but of course, as it hasn't been implemented,
	 * this test should pass as well.
	 */
	@Test
	void testProgramNodeB() {
		ProgramNode pNode = new ProgramNode("foo");
		DeclarationsNode dNode = new DeclarationsNode();
		dNode.addVariable(new VariableNode("num"));
		dNode.addVariable(new VariableNode("f"));
		pNode.setVariables(dNode);
		pNode.setFunctions(new SubProgramDeclarationsNode());
		pNode.setMain(new CompoundStatementNode());
		
		String expected = pNode.indentedToString(0);
		
		String test = "program foo; var num, f : integer; begin end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Testing Compound Statement. This should not return any errors as it is creating the tree correctly.
	 */
	@Test
	void testCompoundStatementA() {
		ProgramNode pNode = new ProgramNode("foo");
		DeclarationsNode dNode = new DeclarationsNode();
		dNode.addVariable(new VariableNode("num"));
		dNode.addVariable(new VariableNode("f"));
		pNode.setVariables(dNode);
		pNode.setFunctions(new SubProgramDeclarationsNode());
		CompoundStatementNode compStatNode = new CompoundStatementNode();
		AssignmentStatementNode aStatNode = new AssignmentStatementNode();
		OperationNode addN = new OperationNode(TokenType.PLUS);
		OperationNode multN = new OperationNode(TokenType.ASTERISK);
		multN.setLeft(new ValueNode("3"));
		multN.setRight(new VariableNode("f"));
		addN.setLeft(multN);
		addN.setRight(new VariableNode("num"));
		aStatNode.setLvalue(new VariableNode("num"));
		aStatNode.setExpression(addN);
		
		compStatNode.addStatement(aStatNode);
		pNode.setMain(compStatNode);
		
		
		String expected = pNode.indentedToString(0);
		
		String test = "program foo; var num, f : integer; begin num := 3 * f + num end .";
		Parser p = new Parser(test, false);
		
		String actual = p.program().indentedToString(0);
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test factor to see if it correctly returns array node.
	 */
	@Test
	void testFactorA() {
		ArrayNode eNode = new ArrayNode("foo");
		eNode.setExpression(new ValueNode("3"));
		String expected = eNode.indentedToString(0);
		
		String test = "foo[3]";
		Parser p = new Parser(test, false);
		
		String actual = p.factor().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test factor to see if it correctly returns procedure node.
	 */
	@Test
	void testFactorB() {
		ProcedureNode pNode = new ProcedureNode("foo");
		pNode.addExpression(new ValueNode("3"));
		String expected = pNode.indentedToString(0);
		
		String test = "foo(3)";
		Parser p = new Parser(test, false);
		
		String actual = p.factor().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test factor to see if it correctly returns value node.
	 */
	@Test
	void testFactorC() {
		ValueNode vNode = new ValueNode("3");
		String expected = vNode.indentedToString(0);
		
		String test = "3";
		Parser p = new Parser(test, false);
		
		String actual = p.factor().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test simple_expression to see if it correctly returns an operationNode.
	 */
	@Test
	void testSimpleExpressionA() {
		OperationNode plusNode = new OperationNode(TokenType.PLUS);
		OperationNode multNode = new OperationNode(TokenType.ASTERISK);
		multNode.setLeft(new VariableNode("f"));
		multNode.setRight(new ValueNode("5"));
		plusNode.setLeft(new ValueNode("3"));
		plusNode.setRight(multNode);
		String expected = plusNode.indentedToString(0);
		
		String test = "3 + f * 5";
		Parser p = new Parser(test, false);
		
		String actual = p.simple_expression().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test simple_expression to see if it correctly returns a procedureNode.
	 */
	@Test
	void testSimpleExpressionB() {
		
		ProcedureNode pNode = new ProcedureNode("foo");
		pNode.addExpression(new VariableNode("fum"));
		pNode.addExpression(new ValueNode("5"));
		String expected = pNode.indentedToString(0);
		
		String test = "foo(fum, 5)";
		Parser p = new Parser(test, false);
		
		String actual = p.simple_expression().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test statement() to see if it correctly returns an AssignmentStatementNode.
	 */
	@Test
	void testStatementA() {
		AssignmentStatementNode aSNode = new AssignmentStatementNode();
		
		OperationNode plusNode = new OperationNode(TokenType.PLUS);
		OperationNode multNode = new OperationNode(TokenType.ASTERISK);
		multNode.setLeft(new VariableNode("foo"));
		multNode.setRight(new ValueNode("5"));
		plusNode.setLeft(multNode);
		plusNode.setRight(new ValueNode("2"));
		
		aSNode.setLvalue(new VariableNode("foo"));
		aSNode.setExpression(plusNode);
		
		String expected = aSNode.indentedToString(0);
		
		String test = "foo := foo * 5 + 2";
		Parser p = new Parser(test, false);
		p.getSymbolTable().add("foo", Kind.VARIABLE);
		
		String actual = p.statement().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test statement() to see if it correctly returns a ProcedureStatementNode.
	 */
	@Test
	void testStatementB() {
		ProcedureStatementNode pSNode = new ProcedureStatementNode("foo");
		
		pSNode.addExpression(new VariableNode("fum"));
		pSNode.addExpression(new ValueNode("2"));
		
		String expected = pSNode.indentedToString(0);
		
		String test = "foo(fum, 2)";
		Parser p = new Parser(test, false);
		p.getSymbolTable().add("foo", Kind.PROCEDURE);
		
		String actual = p.statement().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test statement() to see if it correctly returns an IfStatementNode.
	 */
	@Test
	void testStatementC() {
		IfStatementNode iSNode = new IfStatementNode();
		OperationNode ltNode = new OperationNode(TokenType.LESSTHAN);
		ltNode.setLeft(new VariableNode("foo"));
		ltNode.setRight(new ValueNode("2"));
		
		AssignmentStatementNode fooA = new AssignmentStatementNode();
		OperationNode addNode = new OperationNode(TokenType.PLUS);
		addNode.setLeft(new VariableNode("foo"));
		addNode.setRight(new ValueNode("1"));
		
		fooA.setLvalue(new VariableNode("foo"));
		fooA.setExpression(addNode);
		
		AssignmentStatementNode fooB = new AssignmentStatementNode();
		fooB.setLvalue(new VariableNode("foo"));
		fooB.setExpression(new ValueNode("0"));
		
		iSNode.setTest(ltNode);
		iSNode.setThenStatement(fooA);
		iSNode.setElseStatement(fooB);
		
		String expected = iSNode.indentedToString(0);
		
		String test = "if foo < 2 then foo := foo + 1 else foo := 0";
		Parser p = new Parser(test, false);
		p.getSymbolTable().add("foo", Kind.VARIABLE);
		
		String actual = p.statement().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test statement() to see if it correctly returns an WhileStatementNode.
	 */
	@Test
	void testStatementD() {
		WhileStatementNode wSNode = new WhileStatementNode();
		
		OperationNode ltNode = new OperationNode(TokenType.LESSTHAN);
		ltNode.setLeft(new VariableNode("foo"));
		ltNode.setRight(new ValueNode("2"));
		
		AssignmentStatementNode fooA = new AssignmentStatementNode();
		OperationNode addNode = new OperationNode(TokenType.PLUS);
		addNode.setLeft(new VariableNode("foo"));
		addNode.setRight(new ValueNode("1"));
		
		fooA.setLvalue(new VariableNode("foo"));
		fooA.setExpression(addNode);
		
		wSNode.setTest(ltNode);
		wSNode.setDoStatement((fooA));
		
		String expected = wSNode.indentedToString(0);
		
		String test = "while foo < 2 do foo := foo + 1";
		Parser p = new Parser(test, false);
		p.getSymbolTable().add("foo", Kind.VARIABLE);
		
		String actual = p.statement().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test subprogram_declarations() to see if it correctly returns the appropriate SubProgramDeclarationNode.
	 */
	@Test
	void testSubprogramDeclaration() {
		SubProgramNode subProgNode = new SubProgramNode("foo");
		ParameterStatementNode pSNode = new ParameterStatementNode();
		pSNode.addVariable(new VariableNode("a"));
		subProgNode.setParameters(pSNode);
		
		DeclarationsNode dNode = new DeclarationsNode();
		dNode.addVariable(new VariableNode("fum"));
		subProgNode.setVariables(dNode);
		
		CompoundStatementNode compStatNode = new CompoundStatementNode();
		AssignmentStatementNode aSNode = new AssignmentStatementNode();
		OperationNode oNode = new OperationNode(TokenType.PLUS);
		oNode.setLeft(new VariableNode("a"));
		oNode.setRight(new ValueNode("1"));
		aSNode.setLvalue(new VariableNode("fum"));
		aSNode.setExpression(oNode);
		
		compStatNode.addStatement(aSNode);
		subProgNode.setMain(compStatNode);
		
		String expected = subProgNode.indentedToString(0);
		
		String test = "procedure foo (a : integer); var fum : integer; begin fum := a + 1 end;";
		Parser p = new Parser(test, false);
		
		String actual = p.subprogram_declaration().indentedToString(0);

		assertEquals(expected, actual);
	}
	
	/**
	 * Test declarations() to see if it creates the correct DeclarationsNode.
	 */
	@Test
	void testDeclarations() {
		DeclarationsNode dNode = new DeclarationsNode();
		dNode.addVariable(new VariableNode("fum"));
		dNode.addVariable(new VariableNode("foo"));
		dNode.addVariable(new VariableNode("fee"));
		dNode.addVariable(new VariableNode("fi"));
		dNode.addVariable(new VariableNode("fo"));
		
		String expected = dNode.indentedToString(0);
		
		String test = "var fee, fi, fo : real; var fum, foo : integer";
		Parser p = new Parser(test, false);
		
		String actual = p.declarations(new DeclarationsNode()).indentedToString(0);

		assertEquals(expected, actual);
	}
}
