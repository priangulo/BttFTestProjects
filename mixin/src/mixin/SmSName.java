// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class SmSName extends StartName {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false} ;
    }

    public SmSName setParms (QName arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* QName */
        
        InitChildren () ;
        return (SmSName) this ;
    }

}
