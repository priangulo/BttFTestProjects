// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class DecNameDim extends VariableDeclaratorId {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public Dims getDims () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (Dims) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public DecNameDim setParms (QName arg0, AstOptNode arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* [ Dims ] */
        
        InitChildren () ;
        return (DecNameDim) this ;
    }

}
