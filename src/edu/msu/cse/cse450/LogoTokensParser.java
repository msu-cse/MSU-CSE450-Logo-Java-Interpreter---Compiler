package edu.msu.cse.cse450;
// $ANTLR 3.2 Sep 23, 2009 12:02:23 LogoTokens.g 2011-01-22 14:08:24

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
    // LogoTokens.g:35:1: lexerRule : ( COMMAND )? ID ( COMMENT )? NEWLINE ;
    public final void lexerRule() throws RecognitionException {
        try {
            // LogoTokens.g:36:2: ( ( COMMAND )? ID ( COMMENT )? NEWLINE )
            // LogoTokens.g:36:4: ( COMMAND )? ID ( COMMENT )? NEWLINE
            {
            // LogoTokens.g:36:4: ( COMMAND )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==COMMAND) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // LogoTokens.g:36:4: COMMAND
                    {
                    match(input,COMMAND,FOLLOW_COMMAND_in_lexerRule246); 

                    }
                    break;

            }

            match(input,ID,FOLLOW_ID_in_lexerRule249); 
            // LogoTokens.g:36:16: ( COMMENT )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==COMMENT) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // LogoTokens.g:36:16: COMMENT
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_lexerRule251); 

                    }
                    break;

            }

            match(input,NEWLINE,FOLLOW_NEWLINE_in_lexerRule254); 

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


 

    public static final BitSet FOLLOW_COMMAND_in_lexerRule246 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_lexerRule249 = new BitSet(new long[]{0x0000000000001800L});
    public static final BitSet FOLLOW_COMMENT_in_lexerRule251 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_NEWLINE_in_lexerRule254 = new BitSet(new long[]{0x0000000000000002L});

}