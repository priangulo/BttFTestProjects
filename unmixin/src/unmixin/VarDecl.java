// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class VarDecl extends VariableDeclarator {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public VarAssign getVarAssign () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (VarAssign) node : null ;
    }

    public VariableDeclaratorId getVariableDeclaratorId () {
        
        return (VariableDeclaratorId) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public VarDecl setParms (VariableDeclaratorId arg0, AstOptNode arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* VariableDeclaratorId */
        arg [1] = arg1 ;            /* [ VarAssign ] */
        
        InitChildren () ;
        return (VarDecl) this ;
    }

}
