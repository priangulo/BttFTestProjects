// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class SyncStmt extends SynchronizedStatement {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 3 ;

    public Block getBlock () {
        
        return (Block) arg [1] ;
    }

    public Expression getExpression () {
        
        return (Expression) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, false} ;
    }

    public SyncStmt setParms
    (AstToken tok0, AstToken tok1, Expression arg0, AstToken tok2, Block arg1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "synchronized" */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* Expression */
        tok [2] = tok2 ;            /* ")" */
        arg [1] = arg1 ;            /* Block */
        
        InitChildren () ;
        return (SyncStmt) this ;
    }

}
