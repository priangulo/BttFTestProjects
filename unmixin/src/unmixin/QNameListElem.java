// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class QNameListElem extends AstListNode {

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public QNameListElem setParms (AstToken tok0, QName arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "," */
        return setParms (arg0) ;    /* QName */
    }

    public QNameListElem setParms (QName arg0) {
        
        super.setParms (arg0) ;     /* QName */
        return (QNameListElem) this ;
    }

}
