package parser;
import java.io.IOException;

import scanner.*;

public class Parser {
	
	private Token lookAhead;
	private Scanner scanner;
	
	
	
	
	public void program() {
		
	}
	
	public void match(TokenType expected) {
		if (this.lookAhead.getType() == expected) {
			try {
				this.lookAhead = scanner.nextToken();
				if (this.lookAhead == null) {
					this.lookAhead = new Token("EOF", null);
				}
			} catch (IOException e) {
				error("IOException from Scanner");
			}
		}
	}
	
	public void error (String message) {
		System.out.println("Error " + message);
	}
}
