tree grammar LogoTree;

options {
  tokenVocab = LogoJVM1;
  ASTLabelType = ScopedTree;
  output = template;
}
@header {
  import java.util.logging.Logger;
}

@members {
  MemorySpace mem;
  Logger log = Logger.getLogger("LogoTree");

  ScopedTree getTree(Object o) {
    if(o instanceof ScopedTree) return (ScopedTree) o;
    throw new RuntimeException("Attempted to cast " + o + " to a ScopedTree");
  }
  Boolean isFloat(Object t) {
    return getTree(t).valueType.equals( Type.FLOAT );
  }
  Boolean isInt(ScopedTree t) {
    return getTree(t).valueType.equals( Type.INT );
  }
  Boolean isString(ScopedTree t) {
    return getTree(t).valueType.equals( Type.STRING );
  }
  Boolean isBool(ScopedTree t) {
    return getTree(t).valueType.equals( Type.BOOLEAN );
  }
  String getType(Object t) {
    return getTree(t).valueType.getDescriptor(); 
  }
}

program
  : (lines+=line)+              -> jasminFile(filename={"asdf.txt"},
                                              instructions={$lines},
                                              stack={999},
                                              locals={999})
  ;

// -- Keep track of line numbers so that we can debug in Java
line
  : statement                   -> line(line={((Tree)$statement.start).getLine()}, v={$statement.st})
  ;

statement
  : ^(MAKE BYNAME ID e=expr)        -> assign(varNum={$ID.text},
//                                          v={$e.st},
                                          descr={"Hello, world!"},
                                          id={$ID.text},
                                          line={$MAKE.line})
//  | ^(p=PRINT (
//                e=expr          -> print(v=e,class={getType($e.start)})
//              )+)               -> println()
  | ^(PRINT BYNAME ID) {log.info("print byname id");}             -> print(class={getType($BYNAME)})
  ;
  
expr
  : term
  | ^(o=PLUS l=expr r=expr)     ->  add(a={$l.st}, b={$r.st}, float={isFloat($o)})
  | ^(o=MINUS l=expr r=expr)    ->  sub(a={$l.st}, b={$r.st}, float={isFloat($o)})
  | ^(o=MULT l=expr r=expr)     -> mult(a={$l.st}, b={$r.st}, float={isFloat($o)})
  | ^(o=DIV l=expr r=expr)      ->  div(a={$l.st}, b={$r.st}, float={isFloat($o)})        
  ;

term 
  : v=FLOAT                     ->  ldc(value={$v})
  | v=INTEGER                   ->  ldc(value={$v})
  | ^(BYNAME ID)                ->  ldc(value={"HELLO"})
//  | BYVAL ID                  ->  
  ; 
   
  //  | ^(MAKE ref expr) -> write(it={$r.tree})
//
//
//expr
//  : 
//  ;
//
//ref returns [String name]
//  : ^(BYNAME ID) { $name=$ID.text; }
//  ;
//
//float
//  : ^(FLOAT) 
//  ; 
//int
//  : ^(INTEGER)
//  ;
