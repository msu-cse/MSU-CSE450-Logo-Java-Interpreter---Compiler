tree grammar LogoTree;

options {
  tokenVocab = LogoJVM1;
  ASTLabelType = ScopedTree;
  output = template;
}
@header {
  import java.util.logging.Logger;
  import java.io.File;
}

@members {
  MemorySpace mem;
  File file;
  Logger log = Logger.getLogger("LogoTree.g");
}

// -- Program
program 
@init {
  MemorySpace.scopeStack = $block;
}
  : b=block              -> jasminFile(filename={file==null?"<stdin>":file.getName()},
                                       block={$b.st},
                                       stack={999},
                                       locals={999})
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
  : statement                   -> line(text={$statement.text},line={((Tree)$statement.start).getLine()}, v={$statement.st})
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
                                          v={$e.st})
  | ^(p=PRINT (
                prints+=subprint  -> println(before={$prints})
              )+)
//  | ^(PRINT term)               -> print(v={$term.st}, class={$term.tree})
  ;
  
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
  if( Type.coerceRight($l.t,$r.t) )   rst = %coerce(a={$r.st});
  if( Type.coerceLeft($l.t,$r.t) )    lst = %coerce(a={$l.st});
  
  boolean f = $t.equals(Type.FLOAT);
  switch(o.getType()) {
    case PLUS:  $st = %add(l={lst},  r={rst}, float={f}); break;
    case MINUS: $st = %sub(l={lst},  r={rst}, float={f}); break;
    case MULT:  $st = %mult(l={lst}, r={rst}, float={f}); break;
    case DIV:   $st = %div(l={lst},  r={rst}, float={f}); break;
  }
}
  : ^(o=(PLUS|MINUS|MULT|DIV) l=expr r=expr)
  ;

term returns [Type t]
  : v=FLOAT      {$t=Type.FLOAT;}           -> ldc(value={$v.text})
  | v=INTEGER    {$t=Type.INT;}             -> ldc(value={$v.text})
  | var          {$t=Type.STRING;}          -> ldc(value={$v.text})
  | r=ref        {$t=$r.sym.getValueType();}-> {$ref.st}
  ; 

ref returns [Symbol sym] 
  : ^(BYVAL ID) 
    { $sym = $block::scope.get($ID.text); }
     -> var(varNum={$sym.id}, varName={$sym.name}, 
            float={$sym.valueType == Type.FLOAT},
            int  ={$sym.valueType == Type.INT})
  ;
var returns [String name, Type t] : ^(BYNAME ID) { $name = $ID.text; $t = Type.STRING; };
