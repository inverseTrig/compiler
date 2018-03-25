package syntaxtree;

public class ArrayNode extends VariableNode {
	
	private ExpressionNode expression;

	public ArrayNode(String attr) {
		super(attr);
		
	}

	public ExpressionNode getExpression() {
		return expression;
	}

	public void setExpression(ExpressionNode expression) {
		this.expression = expression;
	}
	
	public String getName() {
		return super.getName();
	}

    @Override
    public String indentedToString( int level) {
        String answer = this.indentation(level);
        answer += "Array: " + this.name + "\n";
        answer += "Expression: " + this.getExpression() + "\n";
        return answer;
    }

}
