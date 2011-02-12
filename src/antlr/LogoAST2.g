/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoAST2;
options {
  output=AST;
  ASTLabelType=CommonTree; // type of $stat.tree ref etc...
}

tokens { 
// -- Invisible
  NIL;
  BLOCK;

// -- Math
  PLUS='+';
  MINUS='-';
  MULT='*';
  DIV='/';
  
// -- Commands
  MODULO='modulo';
  PRINT='print';
  MAKE='make';
  
// -- Logic
  WHILE='while';
  IF='if';
  ELSE='else';
  IFELSE='ifelse';
  AND='and';
  OR='or';
  NOT='not';
  
// -- Comparison
  EQ='==';
  LT='<';
  GT='>';
  LTE='<=';
  GTE='>=';
}

@lexer::header{ package edu.msu.cse.cse450; } 
@header{ package edu.msu.cse.cse450; }

@lexer::members{ 
  public Integer mathopCount = 0;
  public Integer commandCount = 0;
  public Integer idCount = 0;
  public Integer refopCount = 0;
  public Integer numberCount = 0;
  public Integer newlineCount = 0;
  public Integer commentCount = 0;

}

program 
    : (statement? NEWLINE!)+
    ;

statement
    : (expression
        | make
        | print
        | while_
        | if_
        | ifelse_ ) COMMENT?
    ;


val: ':'^ ID;
ref: '"'^ ID;

/******************************
 *       LOGIC CONTROL
 ******************************/

if_
    : 'if'^ expression block else_*
    ;

else_
    : 'else'^ ('if' expression)? block
    ;
    
while_
    : 'while'^ '['! expression ']'! block
    ;

ifelse_
    : 'ifelse'^ expression iftrue=block iffalse=block
    ;

/******************************
 *       STATEMENT BLOCKS
 ******************************/
block
    : '[' (statement|NEWLINE)+ ']' -> ^(BLOCK statement+)
    ;

/******************************
 *       COMMANDS
 ******************************/
make
    : 'make'^ ref expression
    ;

print
    : ( 'print'^ expression )             // Single print
    | ( '('! 'print'^ expression+ ')'! )  // Parenthesized multi-print
    ;


/******************************
 *       EXPRESSIONS
 ******************************/
term
    : val
    | ref
    | '('! expression ')'!
    | NUMBER
    ; 

negation
    : ('not'^)* term
    ;
    
unary
    // : ('+'^|'-'^)* negation // Ignore this for now.
    : negation  
    ;

mult
    : unary (('*'|'/'|'%')^ unary)* 
    ;

modulo
    : ('modulo'^ expression expression)
    | mult
    ;

add
    : modulo (('+'|'-')^ modulo)*
    ;

equality
    : add (( '<' | '>' | '=' | '==' | '<=' | '>=' )^ add)*
    ;

expression
    : equality (('and'|'or')^ equality)*
    ;


/******************************
 *       MISC
 ******************************/
 
fragment ALPHA : ('a'..'z'|'A'..'Z');
fragment DIGIT : '0'..'9';

ID    : (ALPHA|'_') (ALPHA|DIGIT|'_')* { idCount++; };

NUMBER 
      : (DIGIT)+ { numberCount++; };

NEWLINE 
      : '\r'? '\n' { newlineCount++; };

COMMENT
      : ';' ~('\n')* {  commentCount++; $channel=HIDDEN; };

WS    : ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
      ;