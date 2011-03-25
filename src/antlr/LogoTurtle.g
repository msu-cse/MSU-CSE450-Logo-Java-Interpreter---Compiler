/* Authors: Zach Riggle (zach@riggle.me), Brandon Overall (overallb@msu.edu), Kole Reece (reecekol@msu.edu) */
grammar LogoTurtle;
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
        | function_call 
        | function_ 
        | repeat
        | turtle
        | return_ ) COMMENT? NEWLINE?
    ;

statements
    : (statement+)^
    ;


val   
@after  { $tree.valueType = null; }
    : ':'^ ID<ValNode>
    ;

ref
@after { $tree.valueType = Type.STRING; }
    : '"'^ ID<StringNode>
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
      'end' -> ^('to' ID val* block)
    ;

return_
    : 'output' expression
    ;

arguments
    : '('! (val|ref)* ')'!
    ;

function_call
    : ID<CallNode>^ arguments
    ;

/******************************
 *       EXPRESSIONS
 ******************************/
term
    : (val
    | ref
    | '('! expression ')'!
    | number)^
    ; 
    
unary
@after { $tree.valueType = x.tree.valueType;}
    // : ('+'^|'-'^)* negation // Ignore this for now.
    : x=term                                        
    ;

mult
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : x=unary
      { ts.returnType = $x.tree.valueType; }
      (
        ('*'|'/'|'%')^ y=unary
         { ts.add($y.tree); }
      )* 
    ;

modulo
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : 'modulo'^ x=mult y=mult
      { ts.add($x.tree); ts.add($y.tree); }
    | z=mult
      { ts.returnType = $z.tree.valueType; }
    ;

add
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : x=modulo                                        
      { ts.returnType = $x.tree.valueType; } 
      (
        ('+'|'-')^ y=modulo
        { ts.add($x.tree); ts.add($y.tree); }
      )*
    ;

equality
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : x=add
      { ts.returnType = $x.tree.valueType; }                          
      (
        ( '<' | '>' | '=' | '==' | '<=' | '>=' )^ y=add
        { ts.add($x.tree); ts.add($y.tree); }
      )*
    ;

boolean_
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : x=equality
      { ts.returnType = $x.tree.valueType; }
      (
        ('and'|'or')^ y=equality
        { ts.add($x.tree); ts.add($y.tree); }
      )*
    ;

expression
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : ('not'^)* b=boolean_
      { ts.add($b.tree); }                            
    ;

number
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = ts.returnType;}
    : i=int_   { ts.add($i.tree); }
    | f=float_ { ts.add($f.tree); }
    ;

float_
@after {$tree.valueType = Type.FLOAT;}
    : FLOAT <FloatNode>
    ;

int_
@after {$tree.valueType = Type.INT;}
    : INTEGER <IntegerNode>
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

ID    : (ALPHA|'_') (ALPHA|DIGIT|'_')* ;

fragment NUMBER
      : (DIGIT)+;

INTEGER
      : NUMBER
      ;

FLOAT
      : NUMBER '.' NUMBER
//      | '.' NUMBER
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