// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class AST_StmtC extends JakartaSST {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_Stmt getAST_Stmt () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (AST_Stmt) node : null ;
    }

    public AstToken getSTM_BEGIN () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getSTM_END () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public AST_StmtC setParms (AstToken tok0, AstOptNode arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* STM_BEGIN */
        arg [0] = arg0 ;            /* [ AST_Stmt ] */
        tok [1] = tok1 ;            /* STM_END */
        
        InitChildren () ;
        return (AST_StmtC) this ;
    }

}
