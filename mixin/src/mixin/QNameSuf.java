// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class QNameSuf extends PrimarySuffix {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public QNameSuf setParms (AstToken tok0, QName arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "." */
        arg [0] = arg0 ;            /* QName */
        
        InitChildren () ;
        return (QNameSuf) this ;
    }

}
