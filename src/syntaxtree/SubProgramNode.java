package syntaxtree;

public class SubProgramNode extends ProgramNode {
	
	SubProgramType type;
	private ParameterStatementNode parameters;

	public ParameterStatementNode getParameters() {
		return parameters;
	}

	public void setParameters(ParameterStatementNode parameters) {
		this.parameters = parameters;
	}

	public SubProgramType getType() {
		return type;
	}

	public void setType(SubProgramType type) {
		this.type = type;
	}

	public SubProgramNode(String aName) {
		super(aName);
		setVariables(new DeclarationsNode());
		setFunctions(new SubProgramDeclarationsNode());
		setMain(new CompoundStatementNode());
	}

	@Override
	public String indentedToString(int level) {
		String answer = this.indentation( level);
        answer += "Program: " + getName() + "\n";
        answer += getParameters().indentedToString( level + 1);
        answer += getVariables().indentedToString( level + 1);
        answer += getFunctions().indentedToString( level + 1);
        answer += getMain().indentedToString( level + 1);
        return answer;
    }

}
