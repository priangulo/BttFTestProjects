// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class AndExpr extends AndExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public EqualityExpression getEqualityExpression () {
        
        return (EqualityExpression) arg [0] ;
    }

    public MoreAndExpr getMoreAndExpr () {
        
        return (MoreAndExpr) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public AndExpr setParms (EqualityExpression arg0, MoreAndExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* EqualityExpression */
        arg [1] = arg1 ;            /* MoreAndExpr */
        
        InitChildren () ;
        return (AndExpr) this ;
    }

}
