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

The (Software Design Document)[https://github.com/inverseTrig/compiler/blob/master/documentation/SDD.pdf] can be found in /documentation/

The code is for educational purposes only.
