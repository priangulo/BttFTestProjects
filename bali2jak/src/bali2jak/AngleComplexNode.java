// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package bali2jak;

public class AngleComplexNode extends ComplexRegex {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public CodeBlockNode getfindCloseAngle () {
        
        return (CodeBlockNode) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, true} ;
    }

    public AngleComplexNode setParms (CodeBlockNode arg0, AstToken tok0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* findCloseAngle */
        tok [0] = tok0 ;            /* ">" */
        
        InitChildren () ;
        return (AngleComplexNode) this ;
    }

}
