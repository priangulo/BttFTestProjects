// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class IfStmt extends IfStatement {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 3 ;

    public ElseClause getElseClause () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (ElseClause) node : null ;
    }

    public Expression getExpression () {
        
        return (Expression) arg [0] ;
    }

    public Statement getStatement () {
        
        return (Statement) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, false, false} ;
    }

    public IfStmt setParms
    (AstToken tok0, AstToken tok1, Expression arg0, AstToken tok2, Statement arg1, AstOptNode arg2)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "if" */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* Expression */
        tok [2] = tok2 ;            /* ")" */
        arg [1] = arg1 ;            /* Statement */
        arg [2] = arg2 ;            /* [ LOOKAHEAD(1) ElseClause ] */
        
        InitChildren () ;
        return (IfStmt) this ;
    }

}