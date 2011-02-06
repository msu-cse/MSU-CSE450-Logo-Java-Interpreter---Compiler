/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoTokens;
/* Character Patterns */

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
    : (expression|make|print) COMMENT? NEWLINE
    ;

val: ':' ID;
ref: '"' ID;

make
    : 'make' ref expression
    ;

print
    : 'print' expression
    ;

term
    : val
    | '(' expression ')'
    | NUMBER
    ; 

negation
    : 'not'* term
    ;
    
unary
    : ('+'|'-')* negation
    ;

mult
    : unary (('*'|'/'|'%') unary)*
    ;

add
    : mult (('+'|'-') mult)*
    ;

equality
    : add (( '<' | '>' | '==' | '!=' | '<=' | '>=' ) add)*
    ;

expression
    : equality (('and'|'or') equality)*
    ;

fragment ALPHA : ('a'..'z'|'A'..'Z');
fragment DIGIT : '0'..'9';

COMMAND 
      : ('print'|'make') { commandCount++; };

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