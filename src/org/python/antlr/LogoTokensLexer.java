package org.python.antlr;

// $ANTLR 3.2 Sep 23, 2009 12:02:23 LogoTokens.g 2011-01-22 14:08:25

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LogoTokensLexer extends Lexer {
    public static final int REFOP=9;
    public static final int WS=13;
    public static final int NEWLINE=11;
    public static final int MATHOP=8;
    public static final int NUMBER=10;
    public static final int DIGIT=5;
    public static final int ID=7;
    public static final int COMMENT=12;
    public static final int EOF=-1;
    public static final int ALPHA=4;
    public static final int COMMAND=6;

    // delegates
    // delegators

    public LogoTokensLexer() {;} 
    public LogoTokensLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LogoTokensLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "LogoTokens.g"; }

    // $ANTLR start "ALPHA"
    public final void mALPHA() throws RecognitionException {
        try {
            int _type = ALPHA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:5:8: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // LogoTokens.g:5:10: ( 'a' .. 'z' | 'A' .. 'Z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALPHA"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            int _type = DIGIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:6:7: ( ( '0' .. '9' ) )
            // LogoTokens.g:6:9: ( '0' .. '9' )
            {
            // LogoTokens.g:6:9: ( '0' .. '9' )
            // LogoTokens.g:6:10: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "COMMAND"
    public final void mCOMMAND() throws RecognitionException {
        try {
            int _type = COMMAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:9:9: ( ( 'print' | 'make' ) )
            // LogoTokens.g:9:11: ( 'print' | 'make' )
            {
            // LogoTokens.g:9:11: ( 'print' | 'make' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='p') ) {
                alt1=1;
            }
            else if ( (LA1_0=='m') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // LogoTokens.g:9:12: 'print'
                    {
                    match("print"); 


                    }
                    break;
                case 2 :
                    // LogoTokens.g:9:20: 'make'
                    {
                    match("make"); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMAND"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:11:6: ( ( ALPHA | '_' ) ( ALPHA | DIGIT | '_' )* )
            // LogoTokens.g:11:8: ( ALPHA | '_' ) ( ALPHA | DIGIT | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // LogoTokens.g:11:20: ( ALPHA | DIGIT | '_' )*
            loop2:
            do {
                int alt2=4;
                switch ( input.LA(1) ) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                    alt2=1;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt2=2;
                    }
                    break;
                case '_':
                    {
                    alt2=3;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // LogoTokens.g:11:21: ALPHA
            	    {
            	    mALPHA(); 

            	    }
            	    break;
            	case 2 :
            	    // LogoTokens.g:11:27: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;
            	case 3 :
            	    // LogoTokens.g:11:33: '_'
            	    {
            	    match('_'); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "MATHOP"
    public final void mMATHOP() throws RecognitionException {
        try {
            int _type = MATHOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:14:9: ( ( '+' | '-' | '*' | '/' | '%' | '(' | ')' ) )
            // LogoTokens.g:14:11: ( '+' | '-' | '*' | '/' | '%' | '(' | ')' )
            {
            if ( input.LA(1)=='%'||(input.LA(1)>='(' && input.LA(1)<='+')||input.LA(1)=='-'||input.LA(1)=='/' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MATHOP"

    // $ANTLR start "REFOP"
    public final void mREFOP() throws RecognitionException {
        try {
            int _type = REFOP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:16:7: ( ( ':' | '\"' ) )
            // LogoTokens.g:16:9: ( ':' | '\"' )
            {
            if ( input.LA(1)=='\"'||input.LA(1)==':' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REFOP"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:19:2: ( ( DIGIT )+ )
            // LogoTokens.g:19:4: ( DIGIT )+
            {
            // LogoTokens.g:19:4: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // LogoTokens.g:19:5: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:22:9: ( ( '\\r' )? '\\n' )
            // LogoTokens.g:22:11: ( '\\r' )? '\\n'
            {
            // LogoTokens.g:22:11: ( '\\r' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\r') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // LogoTokens.g:22:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:25:5: ( ';' (~ ( '\\n' | '\\r' ) )* )
            // LogoTokens.g:25:9: ';' (~ ( '\\n' | '\\r' ) )*
            {
            match(';'); 
            // LogoTokens.g:25:13: (~ ( '\\n' | '\\r' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // LogoTokens.g:25:13: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // LogoTokens.g:28:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // LogoTokens.g:28:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // LogoTokens.g:1:8: ( ALPHA | DIGIT | COMMAND | ID | MATHOP | REFOP | NUMBER | NEWLINE | COMMENT | WS )
        int alt6=10;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // LogoTokens.g:1:10: ALPHA
                {
                mALPHA(); 

                }
                break;
            case 2 :
                // LogoTokens.g:1:16: DIGIT
                {
                mDIGIT(); 

                }
                break;
            case 3 :
                // LogoTokens.g:1:22: COMMAND
                {
                mCOMMAND(); 

                }
                break;
            case 4 :
                // LogoTokens.g:1:30: ID
                {
                mID(); 

                }
                break;
            case 5 :
                // LogoTokens.g:1:33: MATHOP
                {
                mMATHOP(); 

                }
                break;
            case 6 :
                // LogoTokens.g:1:40: REFOP
                {
                mREFOP(); 

                }
                break;
            case 7 :
                // LogoTokens.g:1:46: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 8 :
                // LogoTokens.g:1:53: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 9 :
                // LogoTokens.g:1:61: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 10 :
                // LogoTokens.g:1:69: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\1\15\1\16\2\15\3\uffff\1\13\3\uffff\1\5\3\uffff\1\5\1\uffff"+
        "\3\5\2\27\1\uffff";
    static final String DFA6_eofS =
        "\30\uffff";
    static final String DFA6_minS =
        "\1\11\4\60\3\uffff\1\12\3\uffff\1\151\3\uffff\1\153\1\uffff\1\156"+
        "\1\145\1\164\2\60\1\uffff";
    static final String DFA6_maxS =
        "\2\172\1\71\2\172\3\uffff\1\12\3\uffff\1\151\3\uffff\1\153\1\uffff"+
        "\1\156\1\145\1\164\2\172\1\uffff";
    static final String DFA6_acceptS =
        "\5\uffff\1\4\1\5\1\6\1\uffff\1\10\1\11\1\12\1\uffff\1\1\1\2\1\7"+
        "\1\uffff\1\10\5\uffff\1\3";
    static final String DFA6_specialS =
        "\30\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\13\1\11\2\uffff\1\10\22\uffff\1\13\1\uffff\1\7\2\uffff\1"+
            "\6\2\uffff\4\6\1\uffff\1\6\1\uffff\1\6\12\2\1\7\1\12\5\uffff"+
            "\32\4\4\uffff\1\5\1\uffff\14\4\1\3\2\4\1\1\12\4",
            "\12\5\7\uffff\32\5\4\uffff\1\5\1\uffff\21\5\1\14\10\5",
            "\12\17",
            "\12\5\7\uffff\32\5\4\uffff\1\5\1\uffff\1\20\31\5",
            "\12\5\7\uffff\32\5\4\uffff\1\5\1\uffff\32\5",
            "",
            "",
            "",
            "\1\21",
            "",
            "",
            "",
            "\1\22",
            "",
            "",
            "",
            "\1\23",
            "",
            "\1\24",
            "\1\25",
            "\1\26",
            "\12\5\7\uffff\32\5\4\uffff\1\5\1\uffff\32\5",
            "\12\5\7\uffff\32\5\4\uffff\1\5\1\uffff\32\5",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ALPHA | DIGIT | COMMAND | ID | MATHOP | REFOP | NUMBER | NEWLINE | COMMENT | WS );";
        }
    }
 

}