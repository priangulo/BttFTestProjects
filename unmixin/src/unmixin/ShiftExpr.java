// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class ShiftExpr extends ShiftExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public AdditiveExpression getAdditiveExpression () {
        
        return (AdditiveExpression) arg [0] ;
    }

    public MoreShiftExpr getMoreShiftExpr () {
        
        return (MoreShiftExpr) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public ShiftExpr setParms (AdditiveExpression arg0, MoreShiftExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* AdditiveExpression */
        arg [1] = arg1 ;            /* MoreShiftExpr */
        
        InitChildren () ;
        return (ShiftExpr) this ;
    }

}
