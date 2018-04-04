package syntaxtree;

/**
 * General representation of any expression.
 * @author heechan
 */

public abstract class ExpressionNode extends SyntaxTreeNode {
	
	public DataType dataType;
	
	public ExpressionNode() {
		this.dataType = null;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
}
