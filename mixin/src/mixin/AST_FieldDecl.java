// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

import java.io.*;

import java.util.*;
import Jakarta.util.*;

public class AST_FieldDecl extends AstList {

    @mixin.R4Feature(mixin.R4Feature.StringAST)

    static public  AST_FieldDecl MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_FieldDecl) parser.parse ("AST_FieldDecl") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }

    @mixin.R4Feature(mixin.R4Feature.mixinbase)

    public void mangleConstructors() {
        AstCursor c = new  AstCursor();
        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() )
            if ( c.node instanceof  ConstructorDeclaration )
                ( ( ConstructorDeclaration ) c.node ).mangleConstructor();
    }

}
