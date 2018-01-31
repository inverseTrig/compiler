package scanner;


/**
 * This Scanner is generated using JFlex tool.
 * Though most of the grunt work is done by JFlex, the major methods (namely yytext() and nextToken()) have been thoroughly tested.
 * 
 */


/* Declarations */


%%
%public
%class			Scanner   	/* Names the produced java file */
%function		nextToken 	/* Renames the yylex() function */
%type			Token     	/* Defines the return type of the scanning function */

%eofval{
	return null;
%eofval}

%{						/* Code to create LookupTable
	LookupTable LUT = new LookupTable();
%}


/* Patterns */

whitespace		= [ \n\t\r]

letter			= [A-Za-z]
digit			= [0-9]
sign			= [\+\-]
E				= [E]

id				= {letter}({letter}|{digit}|"_")*

integers		= {digit}{digit}*
decimals		= {integers}(\.){integers}*
exponents		= ({integers}|{decimals}){E}{sign}{integers}
number			= {integers} | {decimals} | {exponents}

symbol			= (\;|\,|\.|\:|\[|\]|\(|\)|\+|\-|\=|\<|\>|\*|\/|<>|<=|>=|:=)
other			= .




%%
/* Lexical Rules */

{whitespace}	{ /* Ignore Whitespace */
					
				}

{id}			{
					/** FOUND ID */
					
					if (LUT.get(yytext()) != null) return new Token(yytext(), LUT.get(yytext()));
					return new Token(yytext(), TokenType.ID);
				}
				
{number}		{
					/** FOUND NUMBER */
					
					return new Token(yytext(), TokenType.NUMBER);
				}
	
{symbol}		{
					/** FOUND SYMBOL */
					
					return new Token(yytext(), LUT.get(yytext()));
				}

{other}			{
					/** FOUND ILLEGAL LEXEME */
					
					return new Token(yytext(), TokenType.ILLEGAL);

				}















