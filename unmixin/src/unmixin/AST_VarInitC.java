// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class AST_VarInitC extends JakartaSST {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_VarInit getAST_VarInit () {
        
        return (AST_VarInit) arg [0] ;
    }

    public AstToken getVI_BEGIN () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getVI_END () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public AST_VarInitC setParms (AstToken tok0, AST_VarInit arg0, AstToken tok1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* VI_BEGIN */
        arg [0] = arg0 ;            /* AST_VarInit */
        tok [1] = tok1 ;            /* VI_END */
        
        InitChildren () ;
        return (AST_VarInitC) this ;
    }

}
