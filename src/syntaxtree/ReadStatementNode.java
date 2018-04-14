package syntaxtree;

public class ReadStatementNode extends StatementNode {
	
	private VariableNode name;

	public ReadStatementNode(VariableNode vNode) {
		this.name = vNode;
	}
	
	public VariableNode getName() {
		return name;
	}
	
	public void setName(VariableNode name) {
		this.name = name;
	}
	
	@Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "ReadStatementNode:" + "\n";
        answer += name.indentedToString( level + 1);
        return answer;
    }
}
