package syntaxtree;

public class WhileStatementNode extends StatementNode {
	private ExpressionNode test;
    private StatementNode doStatement;
    
    public ExpressionNode getTest() {
        return test;
    }

    public void setTest(ExpressionNode test) {
        this.test = test;
    }

    public StatementNode getDoStatement() {
        return doStatement;
    }

    public void setDoStatement(StatementNode doStatement) {
        this.doStatement = doStatement;
    }
    
    @Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "While\n";
        answer += this.test.indentedToString( level + 1);
        answer += this.doStatement.indentedToString( level + 1);
        return answer;
    }
}
