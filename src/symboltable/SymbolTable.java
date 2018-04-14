package symboltable;

import java.util.HashMap;
import java.util.Map.Entry;

import syntaxtree.DataType;

/**
 * Contains the main for the compiler; primarily to test the integration of the SymbolTable to the parser.
 * @author heechan
 *
 */
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
	 * Returns the datatype using lexeme as key value.
	 * @param lexeme
	 * @return
	 */
	public DataType getDataType(String lexeme) {
		if (this.global.containsKey(lexeme)) return this.global.get(lexeme).getDataType();
		return null;
	}
	
	/**
	 * Returns the datatype using lexeme as key value.
	 * @param lexeme
	 * @return
	 */
	public void setDataType(String lexeme, DataType dataType) {
		if (this.global.containsKey(lexeme)) this.global.get(lexeme).setDataType(dataType);
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
		private DataType dataType;
		
		/**
		 * Constructor for our DataStorage - this holds lexeme and kind.
		 * @param lexeme
		 * @param kind
		 */
		private DataStorage(String lexeme, Kind kind) {
			this.lexeme = lexeme;
			this.kind = kind;
			this.dataType = null;
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

		/**
		 * This API call is for getDataType to use.
		 * If getDataType() is not implemented in SymbolTable, we would have to be calling something like:
		 * st.getValue(lexeme).getDataType(); 
		 * this would be a lengthy function call and this method would have to be public as well.
		 * @return
		 */
		private DataType getDataType() {
			return dataType;
		}

		/**
		 * This API call is for setDataType to use.
		 * @return
		 */
		private void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
		
	}

	/**
	 * Overriding the toString() method so it can be used to print out the SymbolTable into a file later on.
	 */
	@Override
	public String toString() {
		String s = String.format("%-10s\t\t%-10s\t\t%-10s\n", "Key", "Entry", "DataType");
		s += "--------------------------------------------------------------------------------\n";
		for (Entry<String, DataStorage> entry: this.global.entrySet()) {
			s += String.format("%-14s\t\t%-14s\t\t%-14s\n", entry.getKey(), entry.getValue().getKind(), entry.getValue().getDataType());
		}
		return s;
	}
}
