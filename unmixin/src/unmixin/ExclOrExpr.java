// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class ExclOrExpr extends ExclusiveOrExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public AndExpression getAndExpression () {
        
        return (AndExpression) arg [0] ;
    }

    public MoreExclOrExpr getMoreExclOrExpr () {
        
        return (MoreExclOrExpr) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public ExclOrExpr setParms (AndExpression arg0, MoreExclOrExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* AndExpression */
        arg [1] = arg1 ;            /* MoreExclOrExpr */
        
        InitChildren () ;
        return (ExclOrExpr) this ;
    }

}
