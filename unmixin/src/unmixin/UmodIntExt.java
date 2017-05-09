// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodIntExt extends UnmodifiedTypeExtension {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 3 ;

    public IntExtClause getIntExtClause () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (IntExtClause) node : null ;
    }

    public InterfaceMemberDeclarations getInterfaceMemberDeclarations () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (InterfaceMemberDeclarations) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, true, false, true} ;
    }

    public UmodIntExt setParms
    (AstToken tok0, QName arg0, AstOptNode arg1, AstToken tok1, AstOptNode arg2, AstToken tok2)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "interface" */
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* [ IntExtClause ] */
        tok [1] = tok1 ;            /* "{" */
        arg [2] = arg2 ;            /* [ InterfaceMemberDeclarations ] */
        tok [2] = tok2 ;            /* "}" */
        
        InitChildren () ;
        return (UmodIntExt) this ;
    }
   
    public boolean propagateChanges( AST_TypeNameList l, 
                                     InterfaceMemberDeclarations i ) {
 
        boolean updated =  UmInterDecl.pc( arg[1], l, i );
        if ( i!=null && ! ( arg[2].toString().equals( i.toString() ) ) ) {
            arg[2].Replace( i );
            updated = true;
        }
        return updated;
    }

}
