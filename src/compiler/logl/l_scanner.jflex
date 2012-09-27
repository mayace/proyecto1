package compiler.logl;

import java_cup.runtime.Symbol;
import javax.swing.JTextArea;

%%

%class L_Scanner
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

SIMPLE_C	=";"[^\r\n]*{NEWLINE}
MULTI_C		="/*"[^\*\/]*~"*/" 

RW_INCLUDE	="#!include"
VAR			="$"{ID}
CALL		="&"{ID}

%%

<YYINITIAL>{
	{ESPACIO}		{/*nada*/}
	{SIMPLE_C}		{/*COMENTARIO*/}
	{MULTI_C}		{/*COMENTARIO*/}
	
	"defun"			{return symbol(L_Sym.RW_DEFUN,yytext());}
	"setf"			{return symbol(L_Sym.RW_SETF,yytext());}
	"defparameter"	{return symbol(L_Sym.RW_DEFPARAMETER,yytext());}
	"if"			{return symbol(L_Sym.RW_IF,yytext());}
	"elseif"		{return symbol(L_Sym.RW_ELSEIF,yytext());}
	"else"			{return symbol(L_Sym.RW_ELSE,yytext());}
	
	"and"			{return symbol(L_Sym.AND,yytext());}
	"or"			{return symbol(L_Sym.OR,yytext());}
	"not"			{return symbol(L_Sym.NOT,yytext());}
	
	{INT}			{return symbol(L_Sym.INT,yytext());}
	{DOUBLE}		{return symbol(L_Sym.DOUBLE,yytext());}
	{CHAR}			{return symbol(L_Sym.CHAR,yytext());}
	{STRING}		{return symbol(L_Sym.STRING,yytext());}
	{BOOLEAN}		{return symbol(L_Sym.BOOLEAN,yytext());}
	
	{ID}			{return symbol(L_Sym.ID,yytext());}
	{RW_INCLUDE}	{return symbol(L_Sym.RW_INCLUDE,yytext());}
	{VAR}			{return symbol(L_Sym.VAR,yytext());}
	{CALL}			{return symbol(L_Sym.CALL,yytext());}
	
	
	"-"				{return symbol(L_Sym.SUB,yytext());}
	"+"				{return symbol(L_Sym.PLUS,yytext());}
	"*"				{return symbol(L_Sym.MUL,yytext());}
	"/"				{return symbol(L_Sym.DIV,yytext());}
	"^"				{return symbol(L_Sym.EXP,yytext());}
	
	"="				{return symbol(L_Sym.EQUAL,yytext());}
	"!="			{return symbol(L_Sym.NEQUAL,yytext());}
	"=="			{return symbol(L_Sym.DEQUAL,yytext());}
	">="			{return symbol(L_Sym.MAYOR_EQUALQ,yytext());}
	"<="			{return symbol(L_Sym.MENOR_EQUALQ,yytext());}
	"<"				{return symbol(L_Sym.MENORQ,yytext());}
	">"				{return symbol(L_Sym.MAYORQ,yytext());}
	
	
	"("				{return symbol(L_Sym.PAR1,yytext());}
	")"				{return symbol(L_Sym.PAR2,yytext());}
	"."				{return symbol(L_Sym.DOT,yytext());}
	","				{return symbol(L_Sym.COLON,yytext());}
	
	
}

<<EOF>>				{return symbol(L_Sym.EOF,yytext());}

[^]		{console.append("[Error] Illegal character <"+yytext()+"> at line "+(yyline+1)+" column " +(yycolumn+1)+"\n");}
