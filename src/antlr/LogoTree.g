tree grammar LogoTree;

options {
  tokenVocab = LogoJVM1;
  ASTLabelType = ScopedTree;
  output = template;
}
@header {
  import java.util.logging.Logger;
  import java.io.File;
  import java.util.List;
  import java.util.Map;
  import java.util.HashMap;
}

@members {
  MemorySpace mem;
  File file;
  Logger log = Logger.getLogger("LogoTree.g");
}

// -- Program
program 
scope {
  Integer stackCount;
}
@init {
  MemorySpace.scopeStack = $block;
}
@after {
  $st = %jasminFile(filename={file==null?"<stdin>":file.getName()},
                                       block={$b.st},
                                       stack={999},
                                       locals={MemorySpace.nextIndex});
}
  : b=block
  ;

// -- Block scope
block
scope {
  MemorySpace scope;
}
@init {
  $block::scope = new MemorySpace();
}
  : (lines+=line)+ -> block(lines={$lines})        
  ;


// -- Keep track of line numbers so that we can debug in Java
line
  : statement                   -> line(text={$statement.text},
                                        line={((Tree)$statement.start).getLine()}, 
                                        v={$statement.st})
  ;

// -- Each individual statement
statement

  : ^(MAKE v=var e=expr)        
    {
      Symbol sym    = $block::scope.create($v.name);
      sym.valueType = $e.t;
    }
                                  -> assign(varName={sym.name},
                                          varNum={sym.id},
                                          v={$e.st},
                                          type={$e.t})
  | ^(p=PRINT (
                prints+=subprint  -> println(before={$prints})
              )+)
  | turtle                        -> {$turtle.st}
  ;

/******************************
 *       PRINT
 ******************************/
subprint returns [Type t]
@after {
  $t = $e.t;
}
  : e=expr                        -> print(v={$e.st},class={$e.t.primitive})
  ;

expr returns [Type t]
  : m=term   {$t=$m.t;}           -> {$term.st}
  | e=math   {$t=$e.t;}           -> mathDebug(d={$e.desc},v={$e.st})
  ;

/******************************
 *       MATH
 ******************************/
math returns [Type t, String desc]
@after {
  $t    = Type.resolveMath($o.getToken(), $l.t,$r.t);  // Type, for passing upward
  $desc = $o.toStringTree();            // Description
  
  // -- Get ahold of the left and right side.  We might want to change these,
  // and we can't directly modify $x.st
  StringTemplate lst = $l.st;
  StringTemplate rst = $r.st;
  
  // -- Must we do any type conversions?
  Type resultType = Type.resolveMath($l.t,$r.t);
  if( $r.t != resultType )   
    rst = %coerce(a={$r.st},from={$r.t},to={resultType});
  if( $l.t != resultType )    
    lst = %coerce(a={$l.st},from={$l.t},to={resultType});
  
  switch(o.getType()) {
    case PLUS:  $st = %add(l={lst},  r={rst}, type={$r.t}); break;
    case MINUS: $st = %sub(l={lst},  r={rst}, type={$r.t}); break;
    case MULT:  $st = %mult(l={lst}, r={rst}, type={$r.t}); break;
    case DIV:   $st = %div(l={lst},  r={rst}, type={$r.t}); break;
  }
}
  : ^(o=(PLUS|MINUS|MULT|DIV) l=expr r=expr)
  ;

term returns [Type t]
  : v=FLOAT      {$t=Type.FLOAT;}           -> ldc(value={$v.text})
  | v=INTEGER    {$t=Type.INT;}             -> ldc(value={$v.text})
  | var          {$t=Type.STRING;}          -> ldc(value={$var.text +'"'})
  | r=ref        {$t=$r.sym.getValueType();}-> {$ref.st}
  ; 


/******************************
 *       VARIABLES
 ******************************/
ref returns [Symbol sym] 
  : ^(BYVAL ID) 
    { $sym = $block::scope.get($ID.text); }
     -> var(varNum={$sym.id}, varName={$sym.name}, 
            float={$sym.valueType == Type.FLOAT},
            int  ={$sym.valueType == Type.INT})
  ;
var returns [String name, Type t] : ^(BYNAME ID) { $name = $ID.text; $t = Type.STRING; };

/******************************
 *       TURTLE
 ******************************/
turtle
@after {

  TurtleCall fn = (TurtleCall) TurtleCall.methods.get($o);
  Integer expected = fn.argumentTypes.size();  
  List templates = new ArrayList();
    
  
  // -- Are there any arguments?
  if(expected > 0) {
	  
	  // -- Ensure the proper number of operands
	  Integer actual   = args.argList.size();
	  if( ! expected.equals(actual) ) {
	    throw new LogoException("Parameter count mismatch for fn " + fn.name + " at " + o.info() + 
	    ": expected " + expected + ", got " + actual ) ;
	  }
	   
	  // TODO: Ensure parameter types.  For now, the hell with it.
	  
	  for( expr_return e: args.argList )
	    templates.add(e.getTemplate());
  }
  
  
  $st = %turtle(args={templates}, signature={fn.signature}, returntype={fn.returnType.primitive}, argtypes={fn.argumentTypes.toString()} );
}
  :  ^(
        o=(PENUP|PENDOWN|BEGINFILL|ENDFILL|FORWARD|FORWARD2|BACKWARD|BACKWARD2|LEFT|LEFT2|RIGHT|RIGHT2|CIRCLE|SETPOS|COLOR|SETHEADING|SETHEADING2)
        (args=arguments)?
      )
  ;

arguments returns [List<expr_return> argList]
@init {
  $argList = new ArrayList<expr_return>(); 
}
@after {
  log.info(""+$argList);
}
  : (e=expr { $argList.add(e); })+  
  ;