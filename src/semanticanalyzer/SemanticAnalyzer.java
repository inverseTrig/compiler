package semanticanalyzer;

import java.util.ArrayList;

import scanner.TokenType;
import syntaxtree.*;

/**
 * Semantic Analysis class goes through the whole syntax tree produced by the
 * Compiler
 * 
 * @author heechan
 *
 */
public class SemanticAnalyzer {
	private ProgramNode pNode;
	private boolean isFlagged;

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlag(boolean flag) {
		this.isFlagged = flag;
	}

	public SemanticAnalyzer(ProgramNode pNode) {
		this.pNode = pNode;
		this.isFlagged = false;
	}

	public ProgramNode getPNode() {
		return pNode;
	}

	public void Analyze() {
		this.checkVariablesDeclared();
		this.checkVariablesTypes();

		if (!this.getPNode().getFunctions().getProcs().isEmpty()) {
			for (int i = 0; i < this.getPNode().getFunctions().getProcs().size(); i++) {
				SemanticAnalyzer sA = new SemanticAnalyzer(this.getPNode().getFunctions().getProcs().get(i));
				sA.Analyze();
				this.getPNode().getFunctions().getProcs().set(i, (SubProgramNode) sA.getPNode());
			}
		}
	}

	private void checkVariablesTypes() {

		for (StatementNode sN : pNode.getMain().getStatements()) {
			traverseStatementType(sN);
		}
	}

	private DataType traverseStatementType(StatementNode statementNode) {
		if (statementNode instanceof AssignmentStatementNode) {
			traverseExpressionType(((AssignmentStatementNode) statementNode).getLvalue());
			DataType dtL = ((AssignmentStatementNode) statementNode).getLvalue().getDataType();
			DataType dtE = traverseExpressionType(((AssignmentStatementNode) statementNode).getExpression());

			if (dtL != dtE) {
				if (dtL == DataType.REAL || dtE == DataType.REAL) {
					if (dtL == DataType.REAL) {
						((AssignmentStatementNode) statementNode).getExpression().setDataType(DataType.REAL);
					} else if (dtE == DataType.REAL) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName()
								+ " wasn't declared as real, but is being assigned a real.");
						((AssignmentStatementNode) statementNode).getLvalue().setDataType(DataType.REAL);
						isFlagged = true;
						// for (int i = 0; i < pNode.getVariables().getVariable().size(); i ++) {
						// if
						// (pNode.getVariables().getVariable().get(i).getName().equals(((AssignmentStatementNode)
						// statementNode).getLvalue().getName())) {
						// pNode.getVariables().getVariable().get(i).setDataType(DataType.REAL);
						// }
						// }

					}
				} else if (dtL == DataType.INTEGER || dtE == DataType.INTEGER) {
					if (dtL == DataType.INTEGER) {
						System.out.println("Variable " + ((AssignmentStatementNode) statementNode).getLvalue().getName()
								+ " is being assigned to something with unknown datatype.");
						((AssignmentStatementNode) statementNode).getExpression().setDataType(DataType.INTEGER);
						isFlagged = true;
					}
					if (dtE == DataType.INTEGER) {
						// DO NOTHING HERE. THIS SHOULDN'T BE POSSIBLE.
					}
				}
			}

		} else if (statementNode instanceof IfStatementNode) {
			traverseExpressionType(((IfStatementNode) statementNode).getTest());
			traverseStatementType(((IfStatementNode) statementNode).getThenStatement());
			traverseStatementType(((IfStatementNode) statementNode).getElseStatement());
		} else if (statementNode instanceof WhileStatementNode) {
			traverseExpressionType(((WhileStatementNode) statementNode).getTest());
			traverseStatementType(((WhileStatementNode) statementNode).getDoStatement());
		} else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				traverseExpressionType(eN);
			}
		} else if (statementNode instanceof ReadStatementNode) {
			DataType dT = traverseExpressionType(((ReadStatementNode) statementNode).getName());
			((ReadStatementNode) statementNode).getName().setDataType(dT);
		} else if (statementNode instanceof WriteStatementNode) {
			DataType dT = traverseExpressionType(((WriteStatementNode) statementNode).getName());
			((WriteStatementNode) statementNode).getName().setDataType(dT);
		} else if (statementNode instanceof CompoundStatementNode) {
			for (StatementNode sN : ((CompoundStatementNode) statementNode).getStatements()) {
				traverseStatementType(sN);
			}
		}
		return null;
	}

	private DataType traverseExpressionType(ExpressionNode eNode) {
		if (eNode instanceof ValueNode) {
		} else if (eNode instanceof VariableNode) {
		} else if (eNode instanceof OperationNode) {
			DataType dtL = traverseExpressionType(((OperationNode) eNode).getLeft());
			DataType dtR = traverseExpressionType(((OperationNode) eNode).getRight());

			TokenType tType = ((OperationNode) eNode).getOperation();
			if (tType == TokenType.EQUAL || tType == TokenType.NOTEQUAL || tType == TokenType.LESSTHAN
					|| tType == TokenType.LESSTHANOREQUALTO || tType == TokenType.GREATERTHAN
					|| tType == TokenType.GREATERTHANOREQUALTO) {
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

		} else if (eNode instanceof ProcedureNode) {
		}
		return eNode.getDataType();
	}

	private void checkVariablesDeclared() {
		ArrayList<VariableNode> variablesDeclared = new ArrayList<VariableNode>();
		variablesDeclared.addAll(pNode.getVariables().getVariable());
		
		if (this.pNode instanceof SubProgramNode) {
			variablesDeclared.addAll((((SubProgramNode) pNode).getParameters()).getParameter());
		}
		ArrayList<VariableNode> variablesUsed = new ArrayList<VariableNode>();

		for (StatementNode sN : pNode.getMain().getStatements()) {
			variablesUsed.addAll(traverseStatement(sN));
		}

		for (VariableNode sN : variablesUsed) {
			if (!variablesDeclared.contains(sN)) {
				System.out.println("Variable " + sN.getName() + " is used but was never declared.");
				isFlagged = true;
			}
		}

		for (SubProgramNode pN : pNode.getFunctions().getProcs()) {
			SemanticAnalyzer sA = new SemanticAnalyzer(pN);
			sA.checkVariablesDeclared();
		}
	}

	private static ArrayList<VariableNode> traverseStatement(StatementNode statementNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();

		if (statementNode instanceof AssignmentStatementNode) {
			vNodes.add(((AssignmentStatementNode) statementNode).getLvalue());
			vNodes.addAll(traverseExpression(((AssignmentStatementNode) statementNode).getExpression()));
		} else if (statementNode instanceof IfStatementNode) {
			vNodes.addAll(traverseExpression(((IfStatementNode) statementNode).getTest()));
			vNodes.addAll(traverseStatement(((IfStatementNode) statementNode).getThenStatement()));
			vNodes.addAll(traverseStatement(((IfStatementNode) statementNode).getElseStatement()));
		} else if (statementNode instanceof WhileStatementNode) {
			vNodes.addAll(traverseExpression(((WhileStatementNode) statementNode).getTest()));
			vNodes.addAll(traverseStatement(((WhileStatementNode) statementNode).getDoStatement()));
		} else if (statementNode instanceof ProcedureStatementNode) {
			for (ExpressionNode eN : ((ProcedureStatementNode) statementNode).getExpressions()) {
				vNodes.addAll(traverseExpression(eN));
			}
		} else if (statementNode instanceof ReadStatementNode) {
			vNodes.addAll(traverseExpression(((ReadStatementNode) statementNode).getName()));
		} else if (statementNode instanceof WriteStatementNode) {
			vNodes.addAll(traverseExpression(((WriteStatementNode) statementNode).getName()));
		}
		return vNodes;
	}

	private static ArrayList<VariableNode> traverseExpression(ExpressionNode eNode) {
		ArrayList<VariableNode> vNodes = new ArrayList<VariableNode>();
		if (eNode instanceof ValueNode) {
		} else if (eNode instanceof VariableNode) {
			vNodes.add((VariableNode) eNode);
		} else if (eNode instanceof OperationNode) {
			vNodes.addAll(traverseExpression(((OperationNode) eNode).getLeft()));
			vNodes.addAll(traverseExpression(((OperationNode) eNode).getRight()));
		} else if (eNode instanceof ProcedureNode) {
			for (ExpressionNode eN : ((ProcedureNode) eNode).getExpressions()) {
				vNodes.addAll(traverseExpression(eN));
			}
		}
		return vNodes;
	}
}
