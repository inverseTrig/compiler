package syntaxtree;

import java.util.ArrayList;

public class ParameterStatementNode extends SyntaxTreeNode {
	
	private ArrayList<VariableNode> vars = new ArrayList<VariableNode>();
    
    /**
     * Adds a variable to this declaration.
     * @param aVariable The variable node to add to this declaration.
     */
    public void addVariable( VariableNode aVariable) {
        vars.add( aVariable);
    }
    
    /**
     * Creates a String representation of this declarations node and its children.
     * @param level The tree level at which this node resides.
     * @return A String representing this node.
     */
    @Override
    public String indentedToString( int level) {
        String answer = this.indentation( level);
        answer += "Parameters\n";
        for( VariableNode variable : vars) {
            answer += variable.indentedToString( level + 1);
        }
        return answer;
    }
}