// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package unmixin;

import java.io.*;

import java.util.*;
import Jakarta.util.*;

abstract public class AST_Program extends AstNode {
    static public  AST_Program MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_Program) parser.parse ("AST_Program") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
    public void propagateChanges() {
        AstNode.override( "AST_Program.propageChanges", this );
    }

}