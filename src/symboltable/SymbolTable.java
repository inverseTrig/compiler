package symboltable;

import java.util.HashMap;
import java.util.Map.Entry;

public class SymbolTable {
	private HashMap<String, DataStorage> symbolTable;
	
	/**
	 * Constructor for SymbolTable - our SymbolTable will take in a String lexeme and a DataStorage, which is explained below.
	 */
	public SymbolTable() {
		symbolTable = new HashMap<String, DataStorage>();
	}
	
	/**
	 * Adds lexeme and kind into our SymbolTable.
	 * @param lexeme
	 * @param kind
	 */
	public void add(String lexeme, Kind kind) {
		this.symbolTable.put(lexeme, new DataStorage(lexeme, kind));
	}
	
	/**
	 * Returns the kind using lexeme as key value.
	 * @param lexeme
	 * @return
	 */
	public Kind getKind(String lexeme) {
		return this.symbolTable.get(lexeme).getKind();
	}
	
	/**
	 * DataStorage is an object we create to put inside our HashMap.
	 * DataStorage contains String lexeme, which is identical to the lexeme used as key value in the SymbolTable.
	 * Kind is defined as an ENUM, which can be a PROGRAM, VARIABLE, FUNCTION, OR A PROCEDURE.
	 */
	private class DataStorage {
		private String lexeme;
		private Kind kind;
		
		private DataStorage(String lexeme, Kind kind) {
			this.lexeme = lexeme;
			this.kind = kind;
		}
		
		private Kind getKind() {
			return this.kind;
		}
	}

	/**
	 * Overriding the toString() method so it can be used to print out the SymbolTable into a file later on.
	 */
	@Override
	public String toString() {
		String s = "";
		for (Entry<String, DataStorage> entry: this.symbolTable.entrySet()) {
			s += entry.getKey() + "\t" + entry.getValue().getKind() + "\n";
		}
		return s;
	}
	

}
