
package syntaxtree;

/**
 * Represents an if statement in Pascal.
 * An if statement includes a boolean expression, and two statements.
 * @author Erik Steinmetz
 */
public class IfStatementNode extends StatementNode {
    private ExpressionNode test;
    private StatementNode thenStatement;
    private StatementNode elseStatement;

    public ExpressionNode getTest() {
        return test;
    }

    public void setTest(ExpressionNode test) {
        this.test = test;
    }

    public StatementNode getThenStatement() {
        return thenStatement;
    }

    public void setThenStatement(StatementNode thenStatement) {
        this.thenStatement = thenStatement;
    }

    public StatementNode getElseStatement() {
        return elseStatement;
    }

    public void setElseStatement(StatementNode elseStatement) {
        this.elseStatement = elseStatement;
    }
    
    /**
     * Creates a String representation of this node and its children.
     * @param level The tree level at which this node resides.
     * @return A String representing this node.
     */
    @Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "If\n";
        answer += this.test.indentedToString( level + 1);
        answer += this.thenStatement.indentedToString( level + 1);
        answer += this.elseStatement.indentedToString( level + 1);
        return answer;
    }

}
