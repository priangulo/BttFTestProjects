// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodClassExt extends UnmodifiedTypeExtension {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 1 ;

    public ClassBody getClassBody () {
        
        return (ClassBody) arg [2] ;
    }

    public ImplementsClause getImplementsClause () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (ImplementsClause) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, false} ;
    }

    public UmodClassExt setParms
    (AstToken tok0, QName arg0, AstOptNode arg1, ClassBody arg2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "class" */
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* [ ImplementsClause ] */
        arg [2] = arg2 ;            /* ClassBody */
        
        InitChildren () ;
        return (UmodClassExt) this ;
    }
   
    public boolean propagateChanges( ImplementsClause i, 
                                     ClassBody b ) {

        // must laboriously evaluate all individually, as java compiler
                       // optimizes -- and doesn't notice that side-effects occur

        boolean u =  UmodClassDecl.oneChange( arg[1], i );
        u =  UmodClassDecl.oneChange( arg[2], b ) || u;
        return u;
    }

}
