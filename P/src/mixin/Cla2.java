// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class Cla2 extends CastLookahead {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 3 ;

    public AST_QualifiedName getAST_QualifiedName () {
        
        return (AST_QualifiedName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true, true} ;
    }

    public Cla2 setParms
    (AstToken tok0, AST_QualifiedName arg0, AstToken tok1, AstToken tok2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_QualifiedName */
        tok [1] = tok1 ;            /* "[" */
        tok [2] = tok2 ;            /* "]" */
        
        InitChildren () ;
        return (Cla2) this ;
    }

}