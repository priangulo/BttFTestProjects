// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class AST_QualifiedNameElem extends AstListNode {

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public AST_QualifiedNameElem setParms (AstToken tok0, QName arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "." */
        return setParms (arg0) ;    /* QName */
    }

    public AST_QualifiedNameElem setParms (QName arg0) {
        
        super.setParms (arg0) ;     /* QName */
        return (AST_QualifiedNameElem) this ;
    }

}