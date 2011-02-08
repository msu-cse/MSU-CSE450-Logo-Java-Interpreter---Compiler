/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoTokens;
options {output=AST;}
tokens { NIL; }

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
    : (statement|NEWLINE)+
    ;

statement
    : (expression|make|print|while_|if_) COMMENT? NEWLINE
    ;

val: ':'^ ID;
ref: '"'^ ID;

// -- LOGIC CONTROL --

if_
    : 'if' expression '[' statement* ']'
    ;
    
while_
    : 'while' '[' expression ']' '[' statement* ']'
    ;

// -- COMMANDS --
make
    : 'make'^ ref expression
    ;

print
    : 'print'^ expression
    ;

// -- EXPRESSIONS --
term
    : val
    | '(' expression ')' -> ^(expression)
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

add
    : mult (('+'|'-')^ mult)*
    ;

equality
    : add (( '<' | '>' | '==' | '!=' | '<=' | '>=' )^ add)*
    ;

expression
    : equality (('and'|'or')^ equality)*
    ;

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