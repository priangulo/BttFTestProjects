// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package guidsl;

import Jakarta.util.*;
import java.io.*;
import java.util.*;

public class BNot extends NExpr {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public NExpr getNExpr () {
        
        return (NExpr) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public BNot setParms (AstToken tok0, NExpr arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "not" */
        arg [0] = arg0 ;            /* NExpr */
        
        InitChildren () ;
        return (BNot) this ;
    }

    public void visit( Visitor v ) {
        
        v.action( this );
    }

    public node eharvest () {
	    NExpr n = (NExpr) arg[0];
		 return new not( n.eharvest() );
    }

}
