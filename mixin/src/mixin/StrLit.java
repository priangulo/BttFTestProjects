// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class StrLit extends Literal {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 1 ;

    public AstToken getSTRING_LITERAL () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true} ;
    }

    public StrLit setParms (AstToken tok0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* STRING_LITERAL */
        
        InitChildren () ;
        return (StrLit) this ;
    }

}
