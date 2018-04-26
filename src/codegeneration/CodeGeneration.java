package codegeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import scanner.TokenType;
import syntaxtree.*;

/**
 * This class will write the syntaxtree out as MIPS Assembly Language.
 * 
 * @author HeeChan Kang
 */
public class CodeGeneration {

	private static int currentTRegister = 0;
	private static int currentFRegister = 0;
	private static String hex = new String();

	private static String generateHex() {
		Random random = new Random();
		int val = random.nextInt();
		return Integer.toHexString(val);
	}
	
	/**
	 * This method prints out a .asm file into the directory of the project.
	 * @param filename
	 * @param root
	 */
	public static void writeCodeToFile(String filename, ProgramNode root) {
		PrintWriter write;
		try {
			write = new PrintWriter(new BufferedWriter (new FileWriter(filename.substring(0, filename.length() - 4) + ".asm")));
			write.println(writeCodeForRoot(root));
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the code from the root node by writing the outline of the assembly
	 * code, and telling the root node to write its answer into $s0.
	 *
	 * @param root
	 *            The root node of the equation to be written
	 * @return A String of the assembly code.
	 */
	public static String writeCodeForRoot(ProgramNode root) {
		String code = "";
		code += "    .data\n\n"
			 +  "promptuser:    .asciiz \"Enter value: \"" + "\n"
			 +  "newline:       .asciiz \"\\n\"" + "\n";
		if (root.getVariables() != null) { code += writeCode(root.getVariables()); }
	
//		if (root.getFunctions() != null) {
//			for (SubProgramNode spN : root.getFunctions().getProcs()) {
//				if (spN.getVariables() != null) {
//					code += writeCode(spN.getParameters());
//					code += writeCode(spN.getVariables());
//				}
//			}
//		}
		code += "\n\n    .text\nmain:\n";
//		if (root.getFunctions() != null) {
//			for (SubProgramNode spN : root.getFunctions().getProcs()) {
//				code += writeCode(spN) + "\n";
//			}
//		}
		
		if (root.getMain() != null) {
			code += writeCode(root.getMain());
		}
		code += "\njr $ra";
		return (code);
	}
	
	
	/**
	 * Writes code for ParameterStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(ParameterStatementNode node) {
		String code = "";
		ArrayList<VariableNode> variables = node.getParameter();
		for (VariableNode vN : variables) {
			code += String.format("%-10s     .word 0\n", vN.getName() + ":");
		}
		return (code);
	}
	
	/**
	 * Writes code for SubProgramNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(SubProgramNode node) {
		String code = "";
		if (node.getFunctions() != null) {
			for (SubProgramNode spN : node.getFunctions().getProcs()) {
				code += writeCode(spN);
			}
		}
		if (node.getMain() != null) {
			code += writeCode(node.getMain());
		}
		return (code);
	}
	
	/**
	 * Writes code for DeclarationsNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(DeclarationsNode node) {
		String code = "";
		ArrayList<VariableNode> variables = node.getVariable();
		for (VariableNode vN : variables) {
			code += String.format("%-10s     .word 0\n", vN.getName() + ":");
		}
		return (code);
	}
	
	/**
	 * Writes code for CompoundStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(CompoundStatementNode node) {
		String code = "";
		ArrayList<StatementNode> statements = node.getStatements();
		for (StatementNode sN : statements) {
			code += writeCode(sN);
		}
		return (code);
	}
	
	
	/**
	 * Writes code for the given node. This generic write code takes any
	 * StatementNode, and then recasts according to subclass type for dispatching.
	 * 
	 * @param node
	 *            The node for which to write code.
	 */
	public static String writeCode(StatementNode node) {
		String nodeCode = null;

		if (node instanceof AssignmentStatementNode) {
			nodeCode = writeCode((AssignmentStatementNode) node);
		} else if (node instanceof ProcedureStatementNode) {
			nodeCode = writeCode((ProcedureStatementNode) node);
		} else if (node instanceof CompoundStatementNode) {
			nodeCode = writeCode((CompoundStatementNode) node);
		} else if (node instanceof IfStatementNode) {
			nodeCode = writeCode((IfStatementNode) node);
		} else if (node instanceof WhileStatementNode) {
			nodeCode = writeCode((WhileStatementNode) node);
		} else if (node instanceof ReadStatementNode) {
			nodeCode = writeCode((ReadStatementNode) node);
		} else if (node instanceof WriteStatementNode) {
			nodeCode = writeCode((WriteStatementNode) node);
		}
		return (nodeCode);
	}
	
	/** Writes code for AssignmentStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(AssignmentStatementNode node) {
		String code = "# Assignment-Statement\n";
		ExpressionNode expression = node.getExpression();
		if (expression.getDataType() == DataType.REAL) {

			String rightRegister = "$f" + currentFRegister;
			code += writeCode(expression, rightRegister);
			code += "sw      $f" + currentFRegister + ",   " + node.getLvalue() + "\n";
		} else {
			String rightRegister = "$t" + currentTRegister;
			code += writeCode(expression, rightRegister);
			code += "sw      $t" + currentTRegister + ",   " + node.getLvalue() + "\n";
		}
		return (code);
	}

	/**
	 * Writes code for Procedure StatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(ProcedureStatementNode node) {
		String code = null;
		// Still needs to be implemented		
		return ("\n");
	}
	
	/**
	 * Writes code for IfStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(IfStatementNode node) {
		String code = "\n# If-Statement\n";
		hex = generateHex();
		String register = "$t" + currentTRegister;
		code += writeCode(node.getTest(), register);
		code += "beq     " + register + ",   $zero, IfStatementFailID" + hex + "\n";
		
		code += writeCode(node.getThenStatement());
		code += "j	IfStatementPassID" + hex + "\n";
		
		code += "IfStatementFailID" + hex + ":\n";
		code += writeCode(node.getElseStatement());
		code += "IfStatementPassID" + hex + ":\n";
		return (code);
	}
	
	/**
	 * Writes code for WhileStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(WhileStatementNode node) {
		String code = "\n# While-Statement\n";
		hex = generateHex();
		String register = "$t" + currentTRegister;
		code += "WhileID" + hex + ":\n";
		code += writeCode(node.getTest(), register);
		code += "beq     " + register + ",   $zero, WhileCompleteID" + hex + "\n";
		
		code += writeCode(node.getDoStatement());
		code += "j       WhileID" + hex + "\n";
		
		code += "WhileCompleteID" + hex + ":\n";
		
		return (code);
	}
	
	/**
	 * Writes code for ReadStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(ReadStatementNode node) {
		String code = "\n# Read-Statement\n";
		
		code += "li      $v0,   4\n";
		code += "la      $a0,   promptuser\n";
		code += "syscall\n";
		
		DataType dT = node.getName().getDataType();
		if (dT == DataType.INTEGER) {
			code += "li      $v0,   5\n";
			code += "syscall\n";
			code += "sw      $v0,   " + node.getName().getName() + "\n";
		} else if (dT == DataType.REAL) {
			code += "li      $v0,   6\n";
			code += "syscall\n";
			code += "sw      $v0,   " + node.getName().getName() + "\n";
		}
		
		return (code);
	}
	
	/**
	 * Writes codes for WriteStatementNode.
	 * @param node
	 * @return
	 */
	public static String writeCode(WriteStatementNode node) {
		String code = "\n# Write-Statement\n";
		
		if (node.getName() instanceof OperationNode) {
			if (node.getName().getDataType() == DataType.INTEGER) {
				code += writeCode(node.getName(), "$s0");
				code += "li      $v0,   1\n";
				code += "move    $a0,   $s0\n";
				code += "syscall\n";
			}
		}
		else if (node.getName() instanceof VariableNode) {
			code += writeCode(node.getName(), "$s0");
			code += "li      $v0,   1\n";
			code += "move    $a0,   " + node.getName() + "\n";
			code += "syscall\n";
		}
		
		code += "# New Line\n"
				+ "li      $v0,   4\n"
				+ "la      $a0,   newline\n"
				+ "syscall\n";
		
		return code;
	}

	/**
	 * Writes code for the given node. This generic write code takes any
	 * ExpressionNode, and then recasts according to subclass type for dispatching.
	 * 
	 * @param node
	 *            The node for which to write code.
	 * @param reg
	 *            The register in which to put the result.
	 * @return
	 */
	public static String writeCode(ExpressionNode node, String reg) {
		String nodeCode = null;
		if (node instanceof OperationNode) {
			nodeCode = writeCode((OperationNode) node, reg);
		} else if (node instanceof ValueNode) {
			nodeCode = writeCode((ValueNode) node, reg);
		} else if (node instanceof VariableNode) {
			nodeCode = writeCode((VariableNode) node, reg);
		}
		return (nodeCode);
	}

	/**
	 * Writes code for an operations node. The code is written by gathering the
	 * child nodes' answers into a pair of registers, and then executing the op on
	 * those registers, placing the result in the given result register.
	 * 
	 * @param opNode
	 *            The operation node to perform.
	 * @param resultRegister
	 *            The register in which to put the result.
	 * @return The code which executes this operation.
	 */
	public static String writeCode(OperationNode opNode, String resultRegister) {
		String code;
		String leftRegister = "";
		String rightRegister = "";
		ExpressionNode left = opNode.getLeft();
		if (left.getDataType() == DataType.REAL) {
			leftRegister = "$f" + currentFRegister++;
		} else {
			leftRegister = "$t" + currentTRegister++;
		}
		code = writeCode(left, leftRegister);
		ExpressionNode right = opNode.getRight();
		if (left.getDataType() == DataType.REAL) {
			rightRegister = "$f" + currentFRegister++;
		} else {
			rightRegister = "$t" + currentTRegister++;
		}
		code += writeCode(right, rightRegister);
		TokenType kindOfOp = opNode.getOperation();
		if (kindOfOp == TokenType.PLUS) {
			code += "add     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
		}
		else if (kindOfOp == TokenType.MINUS) {
			code += "sub     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
		}
		else if (kindOfOp == TokenType.ASTERISK) {
			code += "mult    " + leftRegister + ",   " + rightRegister + "\n";
			code += "mflo    " + resultRegister + "\n";
		}
		else if (kindOfOp == TokenType.SLASH) {
			code += "div     " + leftRegister + ",   " + rightRegister + "\n";
			code += "mflo    " + resultRegister + "\n";
		}
		else if (kindOfOp == TokenType.LESSTHAN) {
			code += "slt     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
		}
		else if (kindOfOp == TokenType.GREATERTHAN) {
			code += "slt     " + resultRegister + ",   " + rightRegister + ",   " + leftRegister + "\n";
		}
		else if (kindOfOp == TokenType.LESSTHANOREQUALTO) {
			code += "addi    " + rightRegister + ",   " + rightRegister + ",   1\n";
			code += "slt     " + resultRegister + ",   " + leftRegister + ",   " + rightRegister + "\n";
		}
		else if (kindOfOp == TokenType.GREATERTHANOREQUALTO) {
			code += "addi    " + leftRegister + ",   " + leftRegister + ",   1\n";
			code += "slt     " + resultRegister + ",   " + rightRegister + ",   " + leftRegister + "\n";
		}
		else if (kindOfOp == TokenType.EQUAL) {
			hex = generateHex();
			code += "beq     " + rightRegister + ",   " + leftRegister + ",   EqualID" + hex + "\n";
			code += "li      " + resultRegister + ",   " + "0\n";
			code += "j       EndEqualID" + hex + "\n";
			code += "EqualID" + hex + ":\n";
			code += "li      " + resultRegister + ",   " + "1\n";
			code += "EndEqualID" + hex + ":\n";
		}
		else if (kindOfOp == TokenType.NOTEQUAL) {
			hex = generateHex();
			code += "beq     " + rightRegister + ",   " + leftRegister + ",   NotEqualID" + hex + "\n";
			code += "li      " + resultRegister + ",   " + "1\n";
			code += "j       EndNotEqualID" + hex + "\n";
			code += "NotEqualID" + hex + ":\n";
			code += "li      " + resultRegister + ",   " + "0\n";
			code += "EndNotEqualID" + hex + ":\n";
		}

		currentTRegister = 0;
		currentFRegister = 0;
		return (code);
	}

	/**
	 * Writes code for a value node. The code is written by executing an add
	 * immediate with the value into the destination register. Writes code that
	 * looks like addi $reg, $zero, value
	 * 
	 * @param valNode
	 *            The node containing the value.
	 * @param resultRegister
	 *            The register in which to put the value.
	 * @return The code which executes this value node.
	 */
	public static String writeCode(ValueNode valNode, String resultRegister) {
		String value = valNode.getAttribute();
		String code = "addi    " + resultRegister + ",   $zero, " + value + "\n";
		return (code);
	}

	/**
	 * Writes code for a variable node.
	 * @param vNode
	 * @param resultRegister
	 * @return
	 */
	public static String writeCode(VariableNode vNode, String resultRegister) {
		String name = vNode.getName();
		String code = "lw      " + resultRegister + ",   " + name + "\n";
		return (code);
	}
	
	
}
