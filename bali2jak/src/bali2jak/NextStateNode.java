// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package bali2jak;

public class NextStateNode extends NextState {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 2 ;

    public AstToken getBALI_TOKEN () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true} ;
    }

    public NextStateNode setParms (AstToken tok0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* ":" */
        tok [1] = tok1 ;            /* BALI_TOKEN */
        
        InitChildren () ;
        return (NextStateNode) this ;
    }

}