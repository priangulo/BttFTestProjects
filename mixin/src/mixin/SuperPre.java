// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class SuperPre extends PrimaryPrefix {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false} ;
    }

    public SuperPre setParms (AstToken tok0, AstToken tok1, QName arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "super" */
        tok [1] = tok1 ;            /* "." */
        arg [0] = arg0 ;            /* QName */
        
        InitChildren () ;
        return (SuperPre) this ;
    }

    @mixin.R4Feature(mixin.R4Feature.CommonError)


    public void checkForErrors( int stage, String file ) {

        // don't report errors if "super.xxx" appears in quoted text

        if ( stage != 0 ) {
            super.checkForErrors( stage, file );
            return;
        }

		  MethodDcl.seensuper = true;
    }

}
