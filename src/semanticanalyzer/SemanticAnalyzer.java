package semanticanalyzer;

import java.util.ArrayList;

import scanner.TokenType;
import symboltable.SymbolTable;
import syntaxtree.*;

public class SemanticAnalyzer {
	private ProgramNode pNode;
	
	public SemanticAnalyzer(ProgramNode pNode) {
		this.pNode = pNode;
	}
	
	public ProgramNode getPNode() {
		return pNode;
	}
	
	
	public void checkVariablesTypes() {
		
		for (StatementNode sN : pNode.getMain().getStatements()) {
			traverseStatementType(sN);
		}
		System.out.println(pNode.indentedToString(0));
	}
	
	private DataType traverseStatementType(StatementNode statementNode) {
		if (statementNode instanceof AssignmentStatementNode) {
			traverseExpressionType( ((AssignmentStatementNode) statementNode).getLvalue() );
			DataType dtL = ((AssignmentStatementNode) statementNode).getLvalue().getDataType();
			DataType dtE =  traverseExpressionType(((AssignmentStatementNode) statementNode).getExpression());
			
			if (dtL != dtE) {
				if (dtL == DataType.REAL || dtE == DataType.REAL) {
					if (dtL == DataType.REAL) {
						// DO NOTHING HERE
					}
					if (dtE == DataType.REAL) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName() +
								" wasn't declared as real, but is being assigned as one.\nDeclaration has been changed to real.");
						((AssignmentStatementNode) statementNode).getLvalue().setDataType(DataType.REAL);
						for (int i = 0; i < pNode.getVariables().getVariable().size(); i ++) {
							if (pNode.getVariables().getVariable().get(i).getName().equals(((AssignmentStatementNode) statementNode).getLvalue().getName())) {
								pNode.getVariables().getVariable().get(i).setDataType(DataType.REAL);
							}
						}
						
					}
				}
				else if (dtL == DataType.INTEGER || dtE == DataType.INTEGER) {
					if (dtL == DataType.INTEGER) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName() +
								" is being assigned to something with unknown datatype. Setting RHS as integer like LHS.");
						((AssignmentStatementNode) statementNode).getExpression().setDataType(DataType.INTEGER);
					}
					if (dtE == DataType.INTEGER) {
						// DO NOTHING HERE. THIS SHOULDN'T BE POSSIBLE.
					}
				}
			}
			
		}
		else if (statementNode instanceof IfStatementNode) {
			traverseExpressionType( ((IfStatementNode) statementNode).getTest() );
			traverseStatementType( ((IfStatementNode) statementNode).getThenStatement() );
			traverseStatementType( ((IfStatementNode) statementNode).getElseStatement() );
		}
		else if (statementNode instanceof WhileStatementNode) {
			traverseExpressionType( ((WhileStatementNode) statementNode).getTest() );
			traverseStatementType( ((WhileStatementNode) statementNode).getDoStatement() );
		}
		else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				traverseExpressionType( eN );
			}
		}
		return null;
	}
	
	private DataType traverseExpressionType(ExpressionNode eNode) {
		if (eNode instanceof ValueNode) { }
		else if (eNode instanceof VariableNode) { }
		else if (eNode instanceof OperationNode) {
			DataType dtL = traverseExpressionType(((OperationNode) eNode).getLeft() );
			DataType dtR = traverseExpressionType(((OperationNode) eNode).getRight());

			TokenType tType = ((OperationNode) eNode).getOperation();
			if (tType == TokenType.EQUAL || tType == TokenType.NOTEQUAL || tType == TokenType.LESSTHAN || 
					tType == TokenType.LESSTHANOREQUALTO || tType == TokenType.GREATERTHAN || 
					tType == TokenType.GREATERTHANOREQUALTO) {
				eNode.setDataType(DataType.BOOLEAN);
				return DataType.BOOLEAN;
			}

			if (dtL == DataType.REAL || dtR == DataType.REAL) {
				eNode.setDataType(DataType.REAL);
				return DataType.REAL;
			} else if (dtL == DataType.INTEGER || dtR == DataType.INTEGER) {
				eNode.setDataType(DataType.INTEGER);
				return DataType.INTEGER;
			}
			
		}
		else if (eNode instanceof ProcedureNode) { }
		return eNode.getDataType();
	}
	
	
	
	
	
	
	
	
	public void checkVariablesDeclared() {
		ArrayList<VariableNode> variablesDeclared = pNode.getVariables().getVariable();
		if (this.pNode instanceof SubProgramNode) { variablesDeclared.addAll( (((SubProgramNode) pNode).getParameters()).getParameter() ); }
		ArrayList<VariableNode> variablesUsed = new ArrayList<VariableNode>();
		
		for (StatementNode sN : pNode.getMain().getStatements()) {
			variablesUsed.addAll(traverseStatement(sN));
		}
		
		boolean usedUndeclaredVariable = false;
		for (VariableNode sN : variablesUsed) {
			if (!variablesDeclared.contains(sN)) {
				pNode.getVariables().addVariable(sN);

				System.out.println("Variable " + sN.getName() + " is used but was never declared.");
				usedUndeclaredVariable = true;
			}
		}
		if (usedUndeclaredVariable) { System.out.println("The variable(s) above has been declared."); }
		
		for (ProgramNode pN : pNode.getFunctions().getProcs()) {
			SemanticAnalyzer sA = new SemanticAnalyzer(pN);
			sA.checkVariablesDeclared();
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
