// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class IdEscape extends QName {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 3 ;

    public AST_Exp getAST_Exp () {
        
        return (AST_Exp) arg [0] ;
    }

    public AstToken getID_ESCAPE () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true} ;
    }

    public IdEscape setParms
    (AstToken tok0, AstToken tok1, AST_Exp arg0, AstToken tok2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* ID_ESCAPE */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_Exp */
        tok [2] = tok2 ;            /* ")" */
        
        InitChildren () ;
        return (IdEscape) this ;
    }
    public void unmangleIds( int stage ) {
        super.unmangleIds( stage-1 );
    }

}
