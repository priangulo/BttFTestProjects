// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class AST_FieldDeclC extends JakartaSST {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_FieldDecl getAST_FieldDecl () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (AST_FieldDecl) node : null ;
    }

    public AstToken getMTH_BEGIN () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getMTH_END () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public AST_FieldDeclC setParms (AstToken tok0, AstOptNode arg0, AstToken tok1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* MTH_BEGIN */
        arg [0] = arg0 ;            /* [ AST_FieldDecl ] */
        tok [1] = tok1 ;            /* MTH_END */
        
        InitChildren () ;
        return (AST_FieldDeclC) this ;
    }

}
