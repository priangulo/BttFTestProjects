// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package guidsl;

public class OptTerm extends GTerm {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 3 ;

    public AstToken getIDENTIFIER () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, true} ;
    }

    public OptTerm setParms (AstToken tok0, AstToken tok1, AstToken tok2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "[" */
        tok [1] = tok1 ;            /* IDENTIFIER */
        tok [2] = tok2 ;            /* "]" */
        
        InitChildren () ;
        return (OptTerm) this ;
    }

    public void visit( Visitor v ) {
        
        v.action( this );
    }

}
