// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class Ute extends UnmodifiedTypeDeclaration {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public AstToken getREFINES () {
        
        return (AstToken) tok [0] ;
    }

    public UnmodifiedTypeExtension getUnmodifiedTypeExtension () {
        
        return (UnmodifiedTypeExtension) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public Ute setParms (AstToken tok0, UnmodifiedTypeExtension arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* REFINES */
        arg [0] = arg0 ;            /* UnmodifiedTypeExtension */
        
        InitChildren () ;
        return (Ute) this ;
    }
    public boolean canExtract() {
        AstNode.fatalError( tok[0],
               "Unmodified Type Extension in file -- should not be here" );
        return false;
    }

}
