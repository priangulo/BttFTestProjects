// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class ConSSuper extends ExplicitConstructorInvocation {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 4 ;

    public AST_TypeNameList getAST_TypeNameList () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (AST_TypeNameList) node : null ;
    }

    public Arguments getArguments () {
        
        return (Arguments) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, false, true} ;
    }

    public ConSSuper setParms
    (AstToken tok0, AstToken tok1, AstOptNode arg0, AstToken tok2, Arguments arg1, AstToken tok3)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "Super" */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* [ AST_TypeNameList ] */
        tok [2] = tok2 ;            /* ")" */
        arg [1] = arg1 ;            /* Arguments */
        tok [3] = tok3 ;            /* ";" */
        
        InitChildren () ;
        return (ConSSuper) this ;
    }

}