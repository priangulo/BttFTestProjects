// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class CastExpr1 extends CastExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_TypeName getAST_TypeName () {
        
        return (AST_TypeName) arg [0] ;
    }

    public UnaryExpression getUnaryExpression () {
        
        return (UnaryExpression) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true, false} ;
    }

    public CastExpr1 setParms
    (AstToken tok0, AST_TypeName arg0, AstToken tok1, UnaryExpression arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_TypeName */
        tok [1] = tok1 ;            /* ")" */
        arg [1] = arg1 ;            /* UnaryExpression */
        
        InitChildren () ;
        return (CastExpr1) this ;
    }

}
