// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class AST_ExpC extends JakartaSST {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_Exp getAST_Exp () {
        
        return (AST_Exp) arg [0] ;
    }

    public AstToken getEXP_BEGIN () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getEXP_END () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public AST_ExpC setParms (AstToken tok0, AST_Exp arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* EXP_BEGIN */
        arg [0] = arg0 ;            /* AST_Exp */
        tok [1] = tok1 ;            /* EXP_END */
        
        InitChildren () ;
        return (AST_ExpC) this ;
    }

}
