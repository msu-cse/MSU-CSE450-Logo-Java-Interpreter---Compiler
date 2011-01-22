// $ANTLR 3.2 Sep 23, 2009 12:02:23 LogoTokens.g 2011-01-22 15:29:32
 package edu.msu.cse.cse450; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LogoTokensParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHA", "DIGIT", "COMMAND", "ID", "MATHOP", "REFOP", "NUMBER", "NEWLINE", "COMMENT", "WS"
    };
    public static final int REFOP=9;
    public static final int WS=13;
    public static final int NEWLINE=11;
    public static final int MATHOP=8;
    public static final int NUMBER=10;
    public static final int DIGIT=5;
    public static final int COMMENT=12;
    public static final int ID=7;
    public static final int EOF=-1;
    public static final int ALPHA=4;
    public static final int COMMAND=6;

    // delegates
    // delegators


        public LogoTokensParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LogoTokensParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return LogoTokensParser.tokenNames; }
    public String getGrammarFileName() { return "LogoTokens.g"; }



    // $ANTLR start "lexerRule"
    // LogoTokens.g:44:1: lexerRule : ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT )* ( NEWLINE )? EOF ;
    public final void lexerRule() throws RecognitionException {
        try {
            // LogoTokens.g:45:2: ( ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT )* ( NEWLINE )? EOF )
            // LogoTokens.g:45:4: ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT )* ( NEWLINE )? EOF
            {
            // LogoTokens.g:45:4: ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=COMMAND && LA1_0<=NUMBER)||LA1_0==COMMENT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // LogoTokens.g:
            	    {
            	    if ( (input.LA(1)>=COMMAND && input.LA(1)<=NUMBER)||input.LA(1)==COMMENT ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // LogoTokens.g:45:46: ( NEWLINE )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==NEWLINE) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // LogoTokens.g:45:46: NEWLINE
                    {
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_lexerRule282); 

                    }
                    break;

            }

            match(input,EOF,FOLLOW_EOF_in_lexerRule285); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "lexerRule"

    // Delegated rules


 

    public static final BitSet FOLLOW_set_in_lexerRule267 = new BitSet(new long[]{0x0000000000001FC0L});
    public static final BitSet FOLLOW_NEWLINE_in_lexerRule282 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_lexerRule285 = new BitSet(new long[]{0x0000000000000002L});

}