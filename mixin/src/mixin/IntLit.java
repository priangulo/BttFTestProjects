// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class IntLit extends Literal {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 1 ;

    public AstToken getINTEGER_LITERAL () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true} ;
    }

    public IntLit setParms (AstToken tok0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* INTEGER_LITERAL */
        
        InitChildren () ;
        return (IntLit) this ;
    }

}
