package compiler.logp;

import java_cup.runtime.Symbol;
import javax.swing.JTextArea;

%%

%class P_Scanner
%public
%line
%column
%cup


%{
	/*************************
	 * Console
	 *************************/
	JTextArea console	=	new JTextArea();
	public void setconsole(JTextArea console){
		this.console=console;
	}
	public JTextArea getConsole(){
		return this.console;
	}
	/*************************
	 * Symbols
	 *************************/
    /* To create a new java_cup.runtime.Symbol with information about
       the current token, the token will have no value in this
       case. */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    /* Also creates a new java_cup.runtime.Symbol with information
       about the current token, but this object has a value. */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

NEWLINE		=\n|\r|\r\n
ESPACIO     =[ \t]|{NEWLINE}
LETRA		=[a-zñA-ZÑ]
DIGIT		=[0-9]
ID			="_"+({LETRA}|{DIGIT})({LETRA}|{DIGIT}|"_")*|{LETRA}({LETRA}|{DIGIT}|"_")*

INT			="-"?{DIGIT}+
DOUBLE		={INT}"."{INT}
CHAR		="'"[^]"'"
STRING		=\"[^\"]*~\"
BOOLEAN		="verdadero"|"falso"

SIMPLE_C	="##"[^\r\n]*{NEWLINE}
MULTI_C		="/*"[^\*\/]*"*/" 

RW_INCLUDE	="#!include"
VAR			="$"{ID}
CALL		="&"{ID}

%%

<YYINITIAL>{
	{ESPACIO}		{/*nada*/}
	{SIMPLE_C}		{/*COMENTARIO*/}
	{MULTI_C}		{/*COMENTARIO*/}
	
	"sub"			{return symbol(P_Sym.RW_SUB,yytext());}
	"if"			{return symbol(P_Sym.RW_IF,yytext());}
	"elseif"		{return symbol(P_Sym.RW_ELSEIF,yytext());}
	"else"			{return symbol(P_Sym.RW_ELSE,yytext());}
	
	"and"			{return symbol(P_Sym.AND,yytext());}
	"or"			{return symbol(P_Sym.OR,yytext());}
	"not"			{return symbol(P_Sym.NOT,yytext());}
	
	{INT}			{return symbol(P_Sym.INT,yytext());}
	{DOUBLE}		{return symbol(P_Sym.DOUBLE,yytext());}
	{CHAR}			{return symbol(P_Sym.CHAR,yytext());}
	{STRING}		{return symbol(P_Sym.STRING,yytext());}
	{BOOLEAN}		{return symbol(P_Sym.BOOLEAN,yytext());}
	
	{ID}			{return symbol(P_Sym.ID,yytext());}
	{RW_INCLUDE}	{return symbol(P_Sym.RW_INCLUDE,yytext());}
	{VAR}			{return symbol(P_Sym.VAR,yytext());}
	{CALL}			{return symbol(P_Sym.CALL,yytext());}
	
	
	"-"				{return symbol(P_Sym.SUB,yytext());}
	"+"				{return symbol(P_Sym.PLUS,yytext());}
	"*"				{return symbol(P_Sym.MUL,yytext());}
	"/"				{return symbol(P_Sym.DIV,yytext());}
	"^"				{return symbol(P_Sym.EXP,yytext());}
	
	"="				{return symbol(P_Sym.EQUAL,yytext());}
	"!="			{return symbol(P_Sym.NEQUAL,yytext());}
	"=="			{return symbol(P_Sym.DEQUAL,yytext());}
	">="			{return symbol(P_Sym.MAYOR_EQUALQ,yytext());}
	"<="			{return symbol(P_Sym.MENOR_EQUALQ,yytext());}
	"<"				{return symbol(P_Sym.MENORQ,yytext());}
	">"				{return symbol(P_Sym.MAYORQ,yytext());}
	
	
	"("				{return symbol(P_Sym.PAR1,yytext());}
	")"				{return symbol(P_Sym.PAR2,yytext());}
	"{"				{return symbol(P_Sym.LLAVE1,yytext());}
	"}"				{return symbol(P_Sym.LLAVE2,yytext());}
	";"				{return symbol(P_Sym.SEMIC,yytext());}
	"."				{return symbol(P_Sym.DOT,yytext());}
	","				{return symbol(P_Sym.COLON,yytext());}
	
	
}

<<EOF>>				{return symbol(P_Sym.EOF,yytext());}

[^]		{console.append("[Error] Illegal character <"+yytext()+"> at line "+(yyline+1)+" column " +(yycolumn+1)+"\n");}
