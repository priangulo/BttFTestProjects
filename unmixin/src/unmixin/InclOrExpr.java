// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class InclOrExpr extends InclusiveOrExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public ExclusiveOrExpression getExclusiveOrExpression () {
        
        return (ExclusiveOrExpression) arg [0] ;
    }

    public MoreInclOrExpr getMoreInclOrExpr () {
        
        return (MoreInclOrExpr) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public InclOrExpr setParms (ExclusiveOrExpression arg0, MoreInclOrExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* ExclusiveOrExpression */
        arg [1] = arg1 ;            /* MoreInclOrExpr */
        
        InitChildren () ;
        return (InclOrExpr) this ;
    }

}
