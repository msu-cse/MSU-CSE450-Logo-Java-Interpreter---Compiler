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
  
// -- Logic
  WHILE='while';
  IF='if';
  ELSE='else';
  IFELSE='ifelse';
  AND='and';
  OR='or';
  NOT='not';
  REPEAT='repeat';
  
// -- Comparison
  EQ='=';
  LT='<';
  GT='>';
  LTE='<=';
  GTE='>=';

// -- References
  BYVAL=':';
  BYNAME='"';
  
// -- Turtle
  PENUP='penup';
  PENDOWN='pendown';
  FORWARD='forward';
  FORWARD2='fd';
  BACKWARD='back';
  BACKWARD2='bk';
  LEFT='left';
  LEFT2='lt';
  RIGHT='right';
  RIGHT2='rt';
  SETHEADING='setheading';
  SETHEADING2='seth';
  CIRCLE='circle';
  COLOR='setpencolor';
  BEGINFILL='beginfill';
  ENDFILL='endfill';
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
    : (statement? NEWLINE!)+
    ;

statement
    : (expression
        | make
        | print
        | while_
        | if_
        | ifelse_
        | repeat 
        | turtle ) COMMENT?
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
    : 'ifelse'^ expression iftrue=block (NEWLINE?)! iffalse=block
    ;

repeat // 'repeat' is not a class-requried statement, but is nice to have
    : 'repeat'^ expression block
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
 *       TURTLE GRAPHICS
 ******************************/
turtle: ( penup
          | pendown
          | forward
          | backward
          | left
          | right
          | setpos
          | circle
          | color
          | beginfill
          | endfill );
penup:      'penup'^;
pendown:    'pendown'^;
forward:    ('forward'|'fd')^       expression;
backward:   ('back'|'bk')^          expression;
left:       ('left'|'lt')^          expression;
right:      ('right'|'rt')^         expression;
heading:    ('setheading'|'seth')^  expression;
setpos:     'setpos'^               expression expression;
circle:     'circle'                expression;
color:      'setpencolor'^          expression ',' expression ',' expression;
beginfill:  'beginfill';
endfill:    'endfill';

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