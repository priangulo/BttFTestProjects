// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class MDSBlock extends MethodDeclSuffix {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public Block getBlock () {
        
        return (Block) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false} ;
    }

    public MDSBlock setParms (Block arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* Block */
        
        InitChildren () ;
        return (MDSBlock) this ;
    }

}
