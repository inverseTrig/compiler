## Pascal to MIPS Compiler

### Introduction
This project is for Programming Languages and Compilers course, keystone for Computer Science majors at Augsburg University. It contains a compiler in Java for Pascal, to finally, convert the code to MIPS, assembly language. Below are its components:

### Implemented Components
#### /src/scanner/
- Scanner.jflex - a JFlex tool was used to create Scanner.java which currently contains the skeleton of being able to read-in a file and create their respective Token.
- Scanner.java - created via the aforementioned JFlex tool.
- Token.java - this class contains the simple Token object, which contains String lexeme and TokenType.
- TokenType.java - this class is an ENUM that contains all the relative token types in Pascal.
- LookupTable.java - this class extends HashMap, containing String lexeme as a key and TokenType as its value. Each lexeme is matched to their respective TokenType using this LookupTable.
- ScannerTest.java - JUnit testing for yytext() and nextToken().

#### /src/parser/
- Parser.java - contains a parser for Pascal based on the grammar provided by our professor. On top of abiding the rules of the grammar, there are six other methods, isMulop(), mulop(), isAddop(), addop(), isRelop(), and relop() for the sake of readability and simplifying the code.
- ParserTest.java - JUnit testing for six main methods of the Parser; namely program(), declarations(), subprogram_declaration(), statement(), simple_expression(), and factor().
- SymbolTableIntegrationTest.java - JUnit testing for statement() after integrating SymbolTable to the Parser.
- SyntaxTreeTest.java - JUnit testing for the integration of SyntaxTree to the parser.

#### /src/symboltable/
- SymbolTable.java – contains constructor for our symbol table implemented using a HashMap that holds lexeme as a key and an object called DataStructure that holds a lexeme and the kind of the ID that we would like to store.
- Kind.java – This class is an ENUM that contains all the types of ID that we will be storing.
- SymbolTableTest.java – contains JUnit testing cases for SymbolTable.add(lexeme, DataStorage) and SymbolTable.getKind().

#### /src/syntaxtree/
- This package contains the code for our syntax tree provided by our professor, Erik Steinmetz. Each node will contain essential information of the code, which will be used to create a .symboltable file with neatly indented contents. Additional nodes have been created and added by myself.

#### /src/semanticanalyzer/
- This package contains the code that uses the produced syntax tree and checks to see if any of the below conditions are unmet. If a condition is unmet, code generation would not occur and the user would know what needs to be changed:
  - All variables are declared before being used.
  - ExpressionNodes will hold a type, and these types must match across assignments.

#### /src/compiler/
- CompilerMain.java - Contains the main for the compiler; primarily, as of now, to test the production of code in assembly language.

The [Software Design Document](https://github.com/inverseTrig/compiler/blob/master/documentation/SDD.pdf) can be found in /documentation/

The code is for educational purposes only.
