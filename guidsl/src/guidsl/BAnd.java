// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package guidsl;

import Jakarta.util.*;
import java.io.*;
import java.util.*;

public class BAnd extends AExpr {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 ;

    public AExpr getAExpr () {
        
        return (AExpr) arg [1] ;
    }

    public NExpr getNExpr () {
        
        return (NExpr) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, true, false} ;
    }

    public BAnd setParms (NExpr arg0, AstToken tok0, AExpr arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* NExpr */
        tok [0] = tok0 ;            /* "and" */
        arg [1] = arg1 ;            /* AExpr */
        
        InitChildren () ;
        return (BAnd) this ;
    }

    public void visit( Visitor v ) {
        
        v.action( this );
    }

    public node eharvest () {
	    NExpr left = (NExpr) arg[0];
	    AExpr right = (AExpr) arg[1];
       return new and( left.eharvest(), right.eharvest() );
    }

}
