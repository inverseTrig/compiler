package symboltable;

import java.util.HashMap;
import java.util.Map.Entry;

public class SymbolTable {
	private HashMap<String, DataStorage> global;
	
	/**
	 * Constructor for SymbolTable - our SymbolTable will take in a String lexeme and a DataStorage, which is explained below.
	 */
	public SymbolTable() {
		global = new HashMap<String, DataStorage>();
	}
	
	/**
	 * Adds lexeme and kind into our SymbolTable.
	 * @param lexeme
	 * @param kind
	 */
	public void add(String lexeme, Kind kind) {
		this.global.put(lexeme, new DataStorage(lexeme, kind));
	}
	
	/**
	 * Returns the kind using lexeme as key value.
	 * @param lexeme
	 * @return
	 */
	public Kind getKind(String lexeme) {
		if (this.global.containsKey(lexeme)) return this.global.get(lexeme).getKind();
		return null;
	}
	
	/**
	 * Returns boolean if the lexeme passed in is the kind requested.
	 * Returns true if it is, and false otherwise.
	 * @param lexeme
	 * @param kind
	 * @return
	 */
	public boolean isKind(String lexeme, Kind kind) {
		if (this.getKind(lexeme) == kind) {
			return true;
		}
		return false;
	}
	
	/**
	 * DataStorage is an object we create to put inside our HashMap.
	 * DataStorage contains String lexeme, which is identical to the lexeme used as key value in the SymbolTable.
	 * Kind is defined as an ENUM, which can be a PROGRAM, VARIABLE, FUNCTION, OR A PROCEDURE.
	 */
	private class DataStorage {
		private String lexeme;
		private Kind kind;
		
		/**
		 * Constructor for our DataStorage - this holds lexeme and kind.
		 * @param lexeme
		 * @param kind
		 */
		private DataStorage(String lexeme, Kind kind) {
			this.lexeme = lexeme;
			this.kind = kind;
		}
		
		/**
		 * This API call is for getKind to use.
		 * If getKind() is not implemented in SymbolTable, we would have to be calling something like:
		 * st.getValue(lexeme).getKind(); 
		 * this would be a lengthy function call and this method would have to be public as well.
		 * @return
		 */
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
		for (Entry<String, DataStorage> entry: this.global.entrySet()) {
			s += entry.getKey() + "\t" + entry.getValue().getKind() + "\n";
		}
		return s;
	}
	

}
