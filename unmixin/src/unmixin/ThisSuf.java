// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

public class ThisSuf extends PrimarySuffix {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 2 ;

    public boolean[] printorder () {
        
        return new boolean[] {true, true} ;
    }

    public ThisSuf setParms (AstToken tok0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "." */
        tok [1] = tok1 ;            /* "this" */
        
        InitChildren () ;
        return (ThisSuf) this ;
    }

}
