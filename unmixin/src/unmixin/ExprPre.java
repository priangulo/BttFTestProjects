// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class ExprPre extends PrimaryPrefix {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public Expression getExpression () {
        
        return (Expression) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public ExprPre setParms (AstToken tok0, Expression arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "(" */
        arg [0] = arg0 ;            /* Expression */
        tok [1] = tok1 ;            /* ")" */
        
        InitChildren () ;
        return (ExprPre) this ;
    }

}
