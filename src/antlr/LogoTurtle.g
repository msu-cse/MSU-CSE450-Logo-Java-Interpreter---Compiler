/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoTurtle;
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
  TO='to';
  END='end';
  RETURN='return';
  
// -- Logic
  WHILE='while';
  IF='if';
  ELSE='else';
  IFELSE='ifelse';
  AND='and';
  OR='or';
  NOT='not';
  
// -- Comparison
  EQ='=';
  LT='<';
  GT='>';
  LTE='<=';
  GTE='>=';

// -- References
  BYVAL=':';
  BYNAME='"';
}

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
    : statements
    ;

statement
    : (expression
        | make
        | print
        | while_
        | if_
        | ifelse_ 
        | function_ 
        | return_ ) COMMENT?
    ;

statements
    : (statement
        | NEWLINE! )+
    ;


val: ':'^ ID;
ref: '"'^ ID;

/******************************
 *       LOGIC CONTROL
 ******************************/

if_
    : 'if'^ expression '['! block ']'! else_*
    ;

else_
    : 'else'^ ('if' expression)? '['! block ']'!
    ;
    
while_
    : 'while'^ '['! expression ']'! '['! block ']'!
    ;

ifelse_
    : 'ifelse'^ expression '['! iftrue=block ']'! (NEWLINE!)? '['! iffalse=block ']'!
    ;

/******************************
 *       STATEMENT BLOCKS
 ******************************/
block
    : statements -> ^(BLOCK statements+)
    ;

/******************************
 *       COMMANDS
 ******************************/
make
    : 'make'^ ref expression
    ;

print
    : 'print'^ expression             // Single print
    | '('! 'print'^ expression+ ')'!  // Parenthesized multi-print
    ;

function_
    : 'to' ID val*
      block
      'end'
    ;

return_
    : 'return' expression
    ;

/******************************
 *       EXPRESSIONS
 ******************************/
term
    : (val
    | ref
    | '('! expression ')'!
    | NUMBER)^
    ; 
    
unary
    // : ('+'^|'-'^)* negation // Ignore this for now.
    : term  
    ;

mult
    : unary (('*'|'/'|'%')^ unary)* 
    ;

modulo
    : ('modulo'^ mult)? mult
    ;

add
    : modulo (('+'|'-')^ modulo)*
    ;

equality
    : add (( '<' | '>' | '=' | '==' | '<=' | '>=' )^ add)*
    ;

boolean_
    : equality (('and'|'or')^ equality)*
    ;

expression
    : ('not'^)* boolean_
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