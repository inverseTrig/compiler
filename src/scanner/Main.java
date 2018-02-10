package scanner;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import symboltable.Kind;
import symboltable.SymbolTable;



public class Main {

	public static void main(String[] args) {
		/**
		String filename = System.getProperty("user.dir") + "/input.txt";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (Exception e) { e.printStackTrace(); }
        InputStreamReader isr = new InputStreamReader(fis);
        Scanner scanner = new Scanner(isr);
        Token aToken = null;
        do
        {
            try {
                aToken = scanner.nextToken();
            }
            catch( Exception e) { e.printStackTrace();}
            //if( aToken != null && !aToken.equals( ""))
                System.out.println("The token returned was " + aToken);
        } while(aToken != null);
        **/
		
		SymbolTable st = new SymbolTable();
		
		st.add("foo", Kind.PROGRAM);
		st.add("fum", Kind.FUNCTION);
		
		System.out.println(st.getKind("foobar"));
		
		System.out.println("\n" + st.toString());
	}

}
