// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class SmClsExtends extends SmExtendsClause {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_QualifiedName getAST_QualifiedName () {
        
        return (AST_QualifiedName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false} ;
    }

    public SmClsExtends setParms
    (AstToken tok0, AstToken tok1, AST_QualifiedName arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "extends" */
        tok [1] = tok1 ;            /* "class" */
        arg [0] = arg0 ;            /* AST_QualifiedName */
        
        InitChildren () ;
        return (SmClsExtends) this ;
    }
    public  SmExtendsClause repairExtendsClause( String myName ) {
        String xname = ( ( AST_QualifiedName ) arg[0] ).GetName();
        if ( xname.equals( myName ) )
            return null;
        else
            return ( SmExtendsClause ) this ;
    }

}
