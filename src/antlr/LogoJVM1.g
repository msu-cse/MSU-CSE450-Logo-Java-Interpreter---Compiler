/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoJVM1;
options {
  output=AST;
  ASTLabelType=ScopedTree; // type of $stat.tree ref etc...
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
  RETURN='output';
  
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
  BACKWARD2='bk';
  BACKWARD='back';
  BEGINFILL='beginfill';
  CIRCLE='circle';
  COLOR='setpencolor';
  ENDFILL='endfill';
  FORWARD2='fd';
  FORWARD='forward';
  LEFT2='lt';
  LEFT='left';
  PENDOWN='pendown';
  PENUP='penup';
  RIGHT2='rt';
  RIGHT='right';
  SETHEADING2='seth';
  SETHEADING='setheading';
  SETPOS='setpos';
}

@header {
  import java.util.logging.Logger;
}

@members {
  int numOps = 0;
  Logger log =  Logger.getLogger("LogoJVM1.g");
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
//        | function_call 
        | function_ 
        | repeat
        | turtle
        | return_ ) COMMENT? NEWLINE?
    ;

statements
    : (statement+)^
    ;


val
    : ':'^ ID  
    ;

ref 
    : '"'^ ID
    ;

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
    : 'ifelse'^ expression '['! iftrue=block ']'! NEWLINE? '['! iffalse=block ']'!
    ;

repeat // 'repeat' is not a class-requried statement, but is nice to have
    : 'repeat'^ expression '['! block ']'!
    ;

/******************************
 *       STATEMENT BLOCKS
 ******************************/
block
scope { MemorySpace symbols; }
@init { $block::symbols = new MemorySpace(); }
    : statements -> ^(BLOCK statements+)
    ;

/******************************
 *       COMMANDS
 ******************************/
make
    : 'make'^ ref expression
    ;

print
    : 'print' expression            -> ^(PRINT expression)  // Single print
    | '(' 'print' expression+ ')' -> ^(PRINT expression+) // Parenthesized multi-print
    ;

function_
    : 'to' ID val*
      block
      'end' -> ^('to' ID val* block)
    ;

return_
    : 'output' expression
    ;

arguments
    : '('! (val|ref)* ')'!
    ;

//function_call
//    : ID^ arguments
//    ;

/******************************
 *       EXPRESSIONS
 ******************************/
term
    : val
    | ref
    | '('! expression ')'!
    | number
    ; 
    
unary
    : term  
    ;

mult
    : unary
      (
        (MULT^|DIV^) unary
      )* 
    ;

modulo
    : MODULO^ mult mult
    | mult
    ;

add : modulo                            
      (
        (o=PLUS^|o=MINUS^) modulo
      )*
    ;

equality
    : add      
      (
        ( LT^ | GT^ | LTE^ | GTE^ | EQ^ ) add
      )*
    ;

boolean_ 
    : equality
      (
        (AND^|OR^) equality
      )*
    ;

expression
    : (NOT^)* boolean_
    ;

number 
    :FLOAT
    |INTEGER
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
circle:     'circle'^               expression expression;
setpos:     'setpos'^               expression expression;
color:      'setpencolor'^          expression ','! expression ','! expression;
beginfill:  'beginfill';
endfill:    'endfill';

/******************************
 *       MISC
 ******************************/
 
fragment ALPHA : ('a'..'z'|'A'..'Z');
fragment DIGIT : '0'..'9';

ID    : (ALPHA|'_') (ALPHA|DIGIT|'_')* ;

fragment NUMBER
      : (DIGIT)+;

INTEGER
      : NUMBER
      ;

FLOAT
      : NUMBER '.' NUMBER
      ;

NEWLINE 
      : '\r'? '\n' { $channel=HIDDEN; };

COMMENT
      : ';' ~('\n')* { $channel=HIDDEN; };

WS    : ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
      ;