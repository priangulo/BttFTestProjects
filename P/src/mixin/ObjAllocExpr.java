// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class ObjAllocExpr extends AllocationExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 ;

    public AST_QualifiedName getAST_QualifiedName () {
        
        return (AST_QualifiedName) arg [0] ;
    }

    public AllocExprChoices getAllocExprChoices () {
        
        return (AllocExprChoices) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false} ;
    }

    public ObjAllocExpr setParms
    (AstToken tok0, AST_QualifiedName arg0, AllocExprChoices arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "new" */
        arg [0] = arg0 ;            /* AST_QualifiedName */
        arg [1] = arg1 ;            /* AllocExprChoices */
        
        InitChildren () ;
        return (ObjAllocExpr) this ;
    }

}
