// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class ThrowsClauseC extends ThrowsClause {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public AST_TypeNameList getAST_TypeNameList () {
        
        return (AST_TypeNameList) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public ThrowsClauseC setParms (AstToken tok0, AST_TypeNameList arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "throws" */
        arg [0] = arg0 ;            /* AST_TypeNameList */
        
        InitChildren () ;
        return (ThrowsClauseC) this ;
    }

}
