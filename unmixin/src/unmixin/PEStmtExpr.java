// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class PEStmtExpr extends StatementExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public PrimaryExpression getPrimaryExpression () {
        
        return (PrimaryExpression) arg [0] ;
    }

    public StmtExprChoices getStmtExprChoices () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (StmtExprChoices) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public PEStmtExpr setParms (PrimaryExpression arg0, AstOptNode arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* PrimaryExpression */
        arg [1] = arg1 ;            /* [ StmtExprChoices ] */
        
        InitChildren () ;
        return (PEStmtExpr) this ;
    }

}
