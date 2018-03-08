package syntaxtree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import scanner.TokenType;

/**
 * JUnit testing for the SyntaxTree stand-alone.
 * @author heechan
 *
 */
class SyntaxTreeTest {

	/**
	 * Created a SyntaxTree manually for the bitcoin example code provided by professor Steinmetz
	 * and comparing the indentedToString() to the expected tree.
	 */
	@Test
	void testBitcoinExample() {
		
		ProgramNode pNode = new ProgramNode("sample");
		
		// DeclarationsNode
		DeclarationsNode dNode = new DeclarationsNode();
		
		VariableNode dollars = new VariableNode("dollars");
		VariableNode yen = new VariableNode("yen");
		VariableNode bitcoins = new VariableNode("bitcoins");
		
		dNode.addVariable( dollars );
		dNode.addVariable( yen );
		dNode.addVariable( bitcoins );
		
		pNode.setVariables(dNode);
		
		// SubProgramDeclarationsNode
		SubProgramDeclarationsNode sPDNode = new SubProgramDeclarationsNode();
		
		pNode.setFunctions(sPDNode);
		
		// CompoundStatementNode
		CompoundStatementNode cSNode = new CompoundStatementNode();
		
		AssignmentStatementNode asnDollars = new AssignmentStatementNode();
		asnDollars.setLvalue( dollars );
		asnDollars.setExpression(new ValueNode("1000000"));
		
		AssignmentStatementNode asnYen = new AssignmentStatementNode();
		asnYen.setLvalue( yen );
		OperationNode onMultiply = new OperationNode(TokenType.ASTERISK);
		onMultiply.setLeft( dollars );
		onMultiply.setRight(new ValueNode("102"));
		asnYen.setExpression(onMultiply);
		
		AssignmentStatementNode asnBitcoins = new AssignmentStatementNode();
		asnBitcoins.setLvalue( bitcoins );
		OperationNode onDivide = new OperationNode(TokenType.SLASH);
		onDivide.setLeft( dollars );
		onDivide.setRight(new ValueNode("400"));
		asnBitcoins.setExpression(onDivide);
		
		
		cSNode.addStatement(asnDollars);
		cSNode.addStatement(asnYen);
		cSNode.addStatement(asnBitcoins);
		
		pNode.setMain(cSNode);
		
		// Compare the strings
		String expected = "Program: sample\n" + 
				"|-- Declarations\n" + 
				"|-- --- Name: dollars\n" + 
				"|-- --- Name: yen\n" + 
				"|-- --- Name: bitcoins\n" + 
				"|-- SubProgramDeclarations\n" + 
				"|-- Compound Statement\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: dollars\n" + 
				"|-- --- --- Value: 1000000\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: yen\n" + 
				"|-- --- --- Operation: ASTERISK\n" + 
				"|-- --- --- --- Name: dollars\n" + 
				"|-- --- --- --- Value: 102\n" + 
				"|-- --- Assignment\n" + 
				"|-- --- --- Name: bitcoins\n" + 
				"|-- --- --- Operation: SLASH\n" + 
				"|-- --- --- --- Name: dollars\n" + 
				"|-- --- --- --- Value: 400\n";
		
		String returned = pNode.indentedToString(0);
		
		assertEquals(expected, returned);
	}
}
