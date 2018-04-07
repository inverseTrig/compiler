package semanticanalyzer;

import java.util.ArrayList;

import symboltable.SymbolTable;
import syntaxtree.*;

public class SemanticAnalyzer {
	private ProgramNode pNode;
	
	public SemanticAnalyzer(ProgramNode pNode) {
		this.pNode = pNode;
	}
	
	public void checkVariablesDeclared() {
		ArrayList<StatementNode> statements = pNode.getMain().getStatements();
		ArrayList<VariableNode> variablesDeclared = pNode.getVariables().getVariable();
		
		ArrayList<VariableNode> variablesUsed = new ArrayList<VariableNode>();
		
		for (StatementNode sN : statements) {
			variablesUsed.addAll(traverseStatement(sN));
		}

		for (VariableNode sN : variablesUsed) {
			System.out.println(sN.getName());
		}
	}
	
	private static ArrayList<VariableNode> traverseStatement(StatementNode statementNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();
		
		if (statementNode instanceof AssignmentStatementNode) {
			vNodes.add(((AssignmentStatementNode) statementNode).getLvalue());
			vNodes.addAll(traverseExpression(((AssignmentStatementNode) statementNode).getExpression()));
		}
		else if (statementNode instanceof IfStatementNode) {
			vNodes.addAll(traverseExpression(((IfStatementNode) statementNode).getTest()));
			vNodes.addAll(traverseStatement(((IfStatementNode) statementNode).getThenStatement()));
			vNodes.addAll(traverseStatement(((IfStatementNode) statementNode).getElseStatement()));
		}
		else if (statementNode instanceof WhileStatementNode) {
			vNodes.addAll(traverseExpression(((WhileStatementNode) statementNode).getTest()));
			vNodes.addAll(traverseStatement(((WhileStatementNode) statementNode).getDoStatement()));
		}
		else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				vNodes.addAll(traverseExpression(eN));
			}
		}
		return vNodes ;
	}
	
	private static ArrayList<VariableNode> traverseExpression(ExpressionNode eNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();
		if (eNode instanceof ValueNode) { }
		else if (eNode instanceof VariableNode) { vNodes.add((VariableNode) eNode); }
		else if (eNode instanceof OperationNode) {
			vNodes.addAll(traverseExpression(((OperationNode) eNode).getLeft()));
			vNodes.addAll(traverseExpression(((OperationNode) eNode).getRight()));
		}
		else if (eNode instanceof ProcedureNode) {
			for (ExpressionNode eN : ((ProcedureNode) eNode).getExpressions()) {
				vNodes.addAll(traverseExpression(eN));
			}
		}
		return vNodes;
	}
}
