grammar LogoTokens;
/* Character Patterns */

ALPHA 	: ('a'..'z'|'A'..'Z');
DIGIT	: ('0'..'9');


COMMAND : ('print'|'say');

ID  	: (ALPHA|'_') (ALPHA|DIGIT|'_')*
    	;

MATHOP  : ('+'|'-'|'*'|'/'|'%'|'('|')');

REFOP	: (':'|'"');

NUMBER 
	: (DIGIT)+
    	;

NEWLINE : '\r'? '\n';

COMMENT
    :   ';' ~('\n'|'\r')* {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

lexerRule 
	: COMMAND? ID COMMENT? NEWLINE;