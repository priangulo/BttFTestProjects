// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class AiEscape extends AST_ArrayInit {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 3 ;

    public AstToken getAI_ESCAPE () {
        
        return (AstToken) tok [0] ;
    }

    public AST_Exp getAST_Exp () {
        
        return (AST_Exp) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true} ;
    }

    public AiEscape setParms
    (AstToken tok0, AstToken tok1, AST_Exp arg0, AstToken tok2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* AI_ESCAPE */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_Exp */
        tok [2] = tok2 ;            /* ")" */
        
        InitChildren () ;
        return (AiEscape) this ;
    }

    @mixin.R4Feature(mixin.R4Feature.CommonErrorAst)

    public void checkForErrors( int stage, String file ) {
        super.checkForErrors( stage-1, file );
    }

    @mixin.R4Feature(mixin.R4Feature.LocalIdAst)

    public void mangleLocalIds( int stage ) {
        super.mangleLocalIds( stage-1 );
    }

}
