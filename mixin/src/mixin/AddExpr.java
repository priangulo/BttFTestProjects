// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class AddExpr extends AdditiveExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public MoreAddExpr getMoreAddExpr () {
        
        return (MoreAddExpr) arg [1] ;
    }

    public MultiplicativeExpression getMultiplicativeExpression () {
        
        return (MultiplicativeExpression) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public AddExpr setParms (MultiplicativeExpression arg0, MoreAddExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* MultiplicativeExpression */
        arg [1] = arg1 ;            /* MoreAddExpr */
        
        InitChildren () ;
        return (AddExpr) this ;
    }

}
