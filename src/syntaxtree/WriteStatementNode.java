package syntaxtree;

public class WriteStatementNode extends StatementNode {
	
	private ExpressionNode name;

	public WriteStatementNode(ExpressionNode eNode) {
		this.name = eNode;
	}
	
	public ExpressionNode getName() {
		return name;
	}
	
	public void setName(VariableNode name) {
		this.name = name;
	}
	
	@Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "WriteStatementNode:" + "\n";
        answer += name.indentedToString( level + 1);
        return answer;
    }
}
