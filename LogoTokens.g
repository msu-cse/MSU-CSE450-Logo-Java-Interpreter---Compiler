/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoTokens;
/* Character Patterns */

@lexer::header{ package org.python.antlr; } 
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
ALPHA : ('a'..'z'|'A'..'Z');
DIGIT : '0'..'9';

COMMAND 
      : ('print'|'make') { commandCount++; };

ID    : (ALPHA|'_') (ALPHA|DIGIT|'_')* { idCount++; };

MATHOP: ('+'|'-'|'*'|'/'|'%'|'('|')') { mathopCount++; };

REFOP : (':'|'"') { refopCount++; };

NUMBER 
      : (DIGIT)+ { numberCount++; };

NEWLINE 
      : '\r'? '\n' { newlineCount++; };

COMMENT
      : ';' ~('\n'|'\r')* {$channel=HIDDEN; commentCount++; };

WS    : ( ' '
        | '\t'
        | '\r'
        | '\n'
        )+ {$channel=HIDDEN;}
      ;

expression: (COMMAND|ID|MATHOP|REFOP|NUMBER|COMMENT);
program : (expression)* NEWLINE? EOF;