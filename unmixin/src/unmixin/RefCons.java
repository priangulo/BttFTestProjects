// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class RefCons extends ConstructorRefinement {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 5 ;

    public AST_ParList getAST_ParList () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (AST_ParList) node : null ;
    }

    public AST_Stmt getAST_Stmt () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (AST_Stmt) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public AstToken getREFINES () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true, false, true, true, false, true} ;
    }

    public RefCons setParms
    (AstToken tok0, QName arg0, AstToken tok1, AstOptNode arg1, AstToken tok2, AstToken tok3, AstOptNode arg2, AstToken tok4)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* REFINES */
        arg [0] = arg0 ;            /* QName */
        tok [1] = tok1 ;            /* "(" */
        arg [1] = arg1 ;            /* [ AST_ParList ] */
        tok [2] = tok2 ;            /* ")" */
        tok [3] = tok3 ;            /* "{" */
        arg [2] = arg2 ;            /* [  AST_Stmt ] */
        tok [4] = tok4 ;            /* "}" */
        
        InitChildren () ;
        return (RefCons) this ;
    }

}
