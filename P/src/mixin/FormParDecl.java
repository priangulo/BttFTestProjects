// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class FormParDecl extends FormalParameter {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 ;

    public AST_TypeName getAST_TypeName () {
        
        return (AST_TypeName) arg [0] ;
    }

    public VariableDeclaratorId getVariableDeclaratorId () {
        
        return (VariableDeclaratorId) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false} ;
    }

    public FormParDecl setParms
    (AstOptToken tok0, AST_TypeName arg0, VariableDeclaratorId arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* [ "final" ] */
        arg [0] = arg0 ;            /* AST_TypeName */
        arg [1] = arg1 ;            /* VariableDeclaratorId */
        
        InitChildren () ;
        return (FormParDecl) this ;
    }

}
