// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class PEIncDec extends PostfixExpression {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public PEPostIncDec getPEPostIncDec () {
        
        return (PEPostIncDec) arg [1] ;
    }

    public PrimaryExpression getPrimaryExpression () {
        
        return (PrimaryExpression) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public PEIncDec setParms (PrimaryExpression arg0, PEPostIncDec arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* PrimaryExpression */
        arg [1] = arg1 ;            /* PEPostIncDec */
        
        InitChildren () ;
        return (PEIncDec) this ;
    }

}
