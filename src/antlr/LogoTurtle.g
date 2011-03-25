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
}

@members{
  public Type result(Type rt, ScopedTree tree) { 
    return Type.getResultType(rt, tree); 
  }
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
        | return_ ) COMMENT? NEWLINE?
    ;

statements
    : statement+
    ;


val
@after { 
  $tree.valueType = $tree.get($id.text,$tree).getValueType();
}
    : ':'^ id=ID
    ;

ref
@after { $tree.valueType = Type.STRING; }
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
@after {
  // We must assume that child(0).child(0) is the actual name.
  
  if($symname.tree.valueType == Type.STRING && $symname.tree.getChildCount() > 0){
    System.out.println($symname.tree.toStringTree());
    System.out.println($symname.tree.getChild(0).toStringTree());
    $tree.put($symname.tree.getChild(0).toString(), symval);
  }
  else throw new LogoException($tree, "Attempted to assign value to non-string " + $symname.tree);
}
    : 'make'^ symname=ref symval=expression
    ;

print
    : 'print'^ expression             // Single print
    | '('! 'print'^ expression+ ')'!  // Parenthesized multi-print
    ;

parameters
    : val*
    ;

function_
    : 'to' ID parameters
      block
      'end'
    ;

return_
    : 'output' expression
    ;

arguments
    : '('! (val|ref)* ')'!
    ;

function_call
    : ID arguments
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
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : x=unary                                         { ts.add($x.tree); }
      (
        ('*'|'/'|'%')^ y=unary                        { ts.add($y.tree); }
      )* 
    ;

modulo
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : 'modulo'^ x=mult y=mult                         { ts.add($x.tree);
                                                        ts.add($y.tree); }
    | z=mult                                          { ts.add($z.tree); }
    ;

add
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : x=modulo                                        { ts.add($x.tree); } 
      (
        ('+'|'-')^ y=modulo                           { ts.add($y.tree); }
      )*
    ;

equality
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : x=add                                           { ts.add($x.tree); }
      (
        ( '<' | '>' | '=' | '==' | '<=' | '>=' )^ y=add
                                                      { ts.add($y.tree); }
      )*
    ;

boolean_
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : x=equality                                      { ts.add($x.tree); }
      (
        ('and'|'or')^ y=equality                      { ts.add($y.tree); }
      )*
    ;

expression
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : ('not'^)* b=boolean_                            { ts.add($b.tree); }
    ;

number
@init  { TypeSet ts = new TypeSet(); }
@after { $tree.valueType = Type.resolve(ts, $tree);}
    : i=int_                                          { ts.add($i.tree); }
    | f=float_                                        { ts.add($f.tree); }
    ;

float_
@after {$tree.valueType = Type.FLOAT;}
    : FLOAT 
    ;

int_
@after {$tree.valueType = Type.INT;}
    : INTEGER
    ;

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