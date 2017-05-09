// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class WhileStm extends WhileStatement {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 3 ;

    public Expression getExpression () {
        
        return (Expression) arg [0] ;
    }

    public Statement getStatement () {
        
        return (Statement) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, false} ;
    }

    public WhileStm setParms
    (AstToken tok0, AstToken tok1, Expression arg0, AstToken tok2, Statement arg1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "while" */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* Expression */
        tok [2] = tok2 ;            /* ")" */
        arg [1] = arg1 ;            /* Statement */
        
        InitChildren () ;
        return (WhileStm) this ;
    }

}
