// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class ConSuper extends ExplicitConstructorInvocation {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 2 ;

    public Arguments getArguments () {
        
        return (Arguments) arg [1] ;
    }

    public PrimDot getPrimDot () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (PrimDot) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, true, false, true} ;
    }

    public ConSuper setParms
    (AstOptNode arg0, AstToken tok0, Arguments arg1, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* [ LOOKAHEAD(2) PrimDot ] */
        tok [0] = tok0 ;            /* "super" */
        arg [1] = arg1 ;            /* Arguments */
        tok [1] = tok1 ;            /* ";" */
        
        InitChildren () ;
        return (ConSuper) this ;
    }

}
