package compiler.logc;


import java_cup.runtime.Symbol;
import javax.swing.JTextArea;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

import compiler.CeParseUtils;

%%

%class C_Scanner
%public
%line
%column
%cup
%char
%unicode

%{
	/*************************
	 * Utils
	 *************************/
	CeParseUtils utils;
	
	public void setUtils(CeParseUtils utils){
		this.utils=utils;
		this.doc=utils.getDocument();
	}
	/*************************
	 * Styled document
	 *************************/
	 DefaultStyledDocument 	doc;
	/*************************
	 * Console
	 *************************/
	public void print(String text){
		this.utils.getConsole().print(text);
	}
	public void println(String text){
		this.utils.getConsole().println(text);
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
		addFormat(Color.black,false,false,false);
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type, Object value, Color color) {
		addFormat(color,false,false,false);
        return new Symbol(type, yyline, yycolumn, value);
    }
    private void addFormat(Color color,boolean bold,boolean italic,boolean underline){
		if(doc==null){
			return;
		}
		SimpleAttributeSet 	sas	= new SimpleAttributeSet();
		
		//StyleConstants.setFontSize(sas, 22);
		StyleConstants.setForeground(sas, color);
		StyleConstants.setBold(sas, bold);
		StyleConstants.setItalic(sas, italic);
		StyleConstants.setUnderline(sas, underline);
		
		doc.setCharacterAttributes(yychar, yylength(), sas, false);
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

SIMPLE_C	="//"[^\r\n]*{NEWLINE}
MULTI_C		="/*" [^\*\/]* ~"*/" 

KW_INCLUDE	="#!include"{ESPACIO}+ [^\r\n \t]+ {NEWLINE}

%%

<YYINITIAL>{
	{ESPACIO}		{/*nada*/addFormat(Color.black,false,false,false);}
	{SIMPLE_C}		{/*COMENTARIO*/addFormat(Color.gray,false,true,false);}
	{MULTI_C}		{/*COMENTARIO*/}
	
	"void"			{return symbol(C_Sym.KW_VOID,yytext(),Color.blue);}
	"int"			{return symbol(C_Sym.KW_INT,yytext(),Color.blue);}
	"double"		{return symbol(C_Sym.KW_DOUBLE,yytext(),Color.blue);}
	"string"		{return symbol(C_Sym.KW_STRING,yytext(),Color.blue);}
	"char"			{return symbol(C_Sym.KW_CHAR,yytext(),Color.blue);}
	"boolean"		{return symbol(C_Sym.KW_BOOLEAN,yytext(),Color.blue);}
	
	"if"			{return symbol(C_Sym.KW_IF,yytext(),Color.red);}
	"else"			{return symbol(C_Sym.KW_ELSE,yytext(),Color.red);}
	"for"			{return symbol(C_Sym.KW_FOR,yytext(),Color.red);}
	"while"			{return symbol(C_Sym.KW_WHILE,yytext(),Color.red);}
	"break"			{return symbol(C_Sym.KW_BREAK,yytext(),Color.red);}
	"struct"		{return symbol(C_Sym.KW_STRUCT,yytext(),Color.red);}
	"return"		{return symbol(C_Sym.KW_RETURN,yytext(),Color.red);}
	
	"&&"			{return symbol(C_Sym.AND,yytext());}
	"||"			{return symbol(C_Sym.OR,yytext());}
	"!"				{return symbol(C_Sym.NOT,yytext());}
	
	{INT}			{return symbol(C_Sym.INT,yytext(),Color.green);}
	{DOUBLE}		{return symbol(C_Sym.DOUBLE,yytext(),Color.green);}
	{CHAR}			{return symbol(C_Sym.CHAR,yytext(),Color.orange);}
	{STRING}		{return symbol(C_Sym.STRING,yytext(),Color.orange);}
	{BOOLEAN}		{return symbol(C_Sym.BOOLEAN,yytext(),Color.blue);}
	
	{ID}			{return symbol(C_Sym.ID,yytext());}
	{KW_INCLUDE}	{return symbol(C_Sym.KW_INCLUDE,yytext(),Color.blue);}
	
	
	"->"			{return symbol(C_Sym.ARROW,yytext());}
	
	"-"				{return symbol(C_Sym.SUB,yytext());}
	"+"				{return symbol(C_Sym.PLUS,yytext());}
	"*"				{return symbol(C_Sym.MUL,yytext());}
	"/"				{return symbol(C_Sym.DIV,yytext());}
	"^"				{return symbol(C_Sym.EXP,yytext());}
	
	"="				{return symbol(C_Sym.EQ,yytext());}
	"!="			{return symbol(C_Sym.NEQ,yytext());}
	"=="			{return symbol(C_Sym.EQEQ,yytext());}
	">="			{return symbol(C_Sym.MAYOR_EQQ,yytext());}
	"<="			{return symbol(C_Sym.MENOR_EQQ,yytext());}
	"<"				{return symbol(C_Sym.MENORQ,yytext());}
	">"				{return symbol(C_Sym.MAYORQ,yytext());}
	
	
	"("				{return symbol(C_Sym.PAR1,yytext());}
	")"				{return symbol(C_Sym.PAR2,yytext());}
	"{"				{return symbol(C_Sym.LLAVE1,yytext());}
	"}"				{return symbol(C_Sym.LLAVE2,yytext());}
	";"				{return symbol(C_Sym.SEMIC,yytext());}
	"."				{return symbol(C_Sym.DOT,yytext());}
	","				{return symbol(C_Sym.COMA,yytext());}
	
	
}

<<EOF>>				{return symbol(C_Sym.EOF,yytext());}

.|\n	{
			addFormat(Color.red,false,false,true);
			println("[Error] Illegal character <"+yytext()+"> at line "+(yyline+1)+" column " +(yycolumn+1));
		}
