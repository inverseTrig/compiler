package syntaxtree;

import java.util.ArrayList;

public class ProcedureStatementNode extends StatementNode {

    private String name;
    private ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
    
    public ProcedureStatementNode(String name) {
    	this.name = name;
    }
    
    public void addExpression( ExpressionNode expression) {
        this.expressions.add( expression);
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public ArrayList<ExpressionNode> getExpressions() {
		return expressions;
	}
    
	@Override
	public String indentedToString(int level) {
		String answer = this.indentation( level);
        answer += "Procedure: " + getName() + "\n";
        for( ExpressionNode expression : expressions) {
            answer += expression.indentedToString( level + 1);
        }
        return answer;
	}


}
