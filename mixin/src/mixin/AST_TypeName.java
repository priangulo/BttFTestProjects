// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

import java.io.*;

abstract public class AST_TypeName extends AstNode {

    @mixin.R4Feature(mixin.R4Feature.Java)

    public String GetName() {
        AstNode.override( "GetName", this );
        return null; // pacify whiney compiler
    }

    public String Signature() {
        AstNode.override( "Signature", this );
        return null; // pacify whiney compiler
    }

    @mixin.R4Feature(mixin.R4Feature.StringAST)

    static public  AST_TypeName MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_TypeName) parser.parse ("AST_TypeName") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }

}
