// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class DelivDecl extends DelivClause {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 4 ;

    public AST_ParList getAST_ParList () {
        
        return (AST_ParList) arg [0] ;
    }

    public AstToken getDELIVERY () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, true} ;
    }

    public DelivDecl setParms
    (AstToken tok0, AstToken tok1, AST_ParList arg0, AstToken tok2, AstToken tok3)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* DELIVERY */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_ParList */
        tok [2] = tok2 ;            /* ")" */
        tok [3] = tok3 ;            /* ";" */
        
        InitChildren () ;
        return (DelivDecl) this ;
    }

}
