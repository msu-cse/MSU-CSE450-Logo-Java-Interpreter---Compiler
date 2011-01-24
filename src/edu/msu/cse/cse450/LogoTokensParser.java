// $ANTLR 3.3 Nov 30, 2010 12:50:56 LogoTokens.g 2011-01-24 16:32:11
 package edu.msu.cse.cse450; 

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LogoTokensParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHA", "DIGIT", "COMMAND", "ID", "MATHOP", "REFOP", "NUMBER", "NEWLINE", "COMMENT", "WS"
    };
    public static final int EOF=-1;
    public static final int ALPHA=4;
    public static final int DIGIT=5;
    public static final int COMMAND=6;
    public static final int ID=7;
    public static final int MATHOP=8;
    public static final int REFOP=9;
    public static final int NUMBER=10;
    public static final int NEWLINE=11;
    public static final int COMMENT=12;
    public static final int WS=13;

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



    // $ANTLR start "expression"
    // LogoTokens.g:46:1: expression : ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT | WS ) ;
    public final void expression() throws RecognitionException {
        try {
            // LogoTokens.g:46:11: ( ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT | WS ) )
            // LogoTokens.g:46:13: ( COMMAND | ID | MATHOP | REFOP | NUMBER | COMMENT | WS )
            {
            if ( (input.LA(1)>=COMMAND && input.LA(1)<=NUMBER)||(input.LA(1)>=COMMENT && input.LA(1)<=WS) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


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
    // $ANTLR end "expression"


    // $ANTLR start "program"
    // LogoTokens.g:47:1: program : ( expression )+ ;
    public final void program() throws RecognitionException {
        try {
            // LogoTokens.g:47:9: ( ( expression )+ )
            // LogoTokens.g:47:11: ( expression )+
            {
            // LogoTokens.g:47:11: ( expression )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=COMMAND && LA1_0<=NUMBER)||(LA1_0>=COMMENT && LA1_0<=WS)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // LogoTokens.g:47:11: expression
            	    {
            	    pushFollow(FOLLOW_expression_in_program299);
            	    expression();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


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
    // $ANTLR end "program"

    // Delegated rules


 

    public static final BitSet FOLLOW_set_in_expression278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_program299 = new BitSet(new long[]{0x00000000000037C2L});

}