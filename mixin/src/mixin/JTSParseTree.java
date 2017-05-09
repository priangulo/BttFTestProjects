package mixin;

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.*;
import Jakarta.util.*;

import java.util.Hashtable;
import Jakarta.util.Util2;

// end Main class

@mixin.R4Feature(mixin.R4Feature.CommonBase)
public class JTSParseTree {

    @mixin.R4Feature(mixin.R4Feature.CommonBase)


    public  AST_Program root = null;
    public boolean isExtension; // is reset by refining constructor

    // AST_Program root inherited from preprocess or mixinbase --
    // couldn't figure out how to do it any other way

    static  Parser myParser;
    public String filepath = null; // file that was parsed
    // private boolean isExtension;

    public static void setFlags( boolean typesort, boolean keysort ) {
        Main.typeSort = typesort;
        Main.keySort = keysort;
    }

    public JTSParseTree( String filename ) throws Exception {

        // set the file name before doing anything else
        // set its absolute path if all goes well.

        kernelConstants.globals().currentFileName = filename;
        FileInputStream     baseFile = null; // base file
        baseFile = new FileInputStream( filename );
        filepath = new File( filename ).getAbsolutePath();
        kernelConstants.globals().currentAbsPath = filepath;
 
        myParser =  Parser.getInstance( baseFile ) ;

        root = (AST_Program) myParser.parseAll () ;
        kernelConstants.globals().currentFileName = filename;
        kernelConstants.globals().currentAbsPath = filepath;

        // finally do customizable processing, and then see if
                  // the parsed file is an extension

        preprocessTree( root ); // phase 1
        phase2( root ); // phase 2
        isExtension = root.isExtension();
    }

    public boolean isExtension() {
        return isExtension;
    }

    public void print() {
        print2file( ( String ) null );
    }

    public void print2file( String filename ) {
        AstProperties props =  AstProperties.open( filename );
        root.print( props );
        props.close();
    }

    public void print2file( File f ) {
        print2file( f.toString() );
    }

    public void print2file( Writer w ) {
        AstProperties props =  AstProperties.open( w );
        root.print( props );
        props.close();
    }

    public String getAspectName() {
        return root.getAspectName();
    }

    public void setAspectName( String pname ) {
        root.setAspectName( pname );
    }

    public void setPackageName( String pname ) {
        System.err.println( "setPackageName deprecated -- use setAspectName" );
        root.setAspectName( pname );
    }

    public static int errorCount() {
        return AstNode.errorCount();
    }

    public static int warningCount() {
        return Jakarta.util.Util.warningCount();
    }

    public String errorCountString() {
        int ecount =  AstNode.errorCount();
        return "Summary "+ ecount + " error" + ( ecount==1?"":"s" );
    }

    public static void resetCounters() {
        Jakarta.util.Util.resetCounters();
    }

    public static void setReportStream( PrintWriter p ) {
        Jakarta.util.Util.setReportStream( p );
    }

    public static void report( String msg ) {
        AstNode.report( msg );
    }

    @mixin.R4Feature(mixin.R4Feature.mixinbase)


    public  TypeDeclaration firstType; // used for inheritance
    public  TypeDeclaration lastType;

    public void phase2( AST_Program root ) throws Exception {
        try {
            root.prepare( ( JTSParseTree ) this );
        }
        catch ( Exception e ) {
            e.printStackTrace();
            throw e;
        }
    }

    public void compose( JTSParseTree t ) {
        // apply inheritance composition rule: const o whatever = const
        // we could do better here, and make suer that the name of the
        // const class, interface, etc. matches the name of whatever.
        // in fact, we should.  this will do for now.

        if ( t.isExtension() )
            root.compose( t.root, ( JTSParseTree ) this, t );
        else {
            AstNode.warning( "overrides previous results" );
            root = t.root;
            firstType = t.firstType;
            lastType  = t.lastType;
            isExtension = t.isExtension;
        }
    }

    public void preprocessTree( AST_Program root ) throws Exception {
		if(R4Feature.AspectDecl) {
			root.checkAspect( filepath );
		}
        //preprocessTree$$LocalId( root );
		
		if (R4Feature.CommonError) {
			// Step 1: check for errors, like ConSuper and SuperPre

			root.checkForErrors( 0, filepath );

			// Step 2: do everything we did before
		}

        //preprocessTree$$CommonBase( root );
		
		if (R4Feature.CommonBase) {
			// Step 1: tag each node of the tree with the name of
			//         its origin (i.e. layername)

			root.setSource( root.getAspectName() );
		}		
		
        // preprocess means -- do everything you did before
        // and then harvest local ids, and then mangle their names

		// preprocessTree$$CommonError( root );
		if (R4Feature.LocalId) {
				root.harvestLocalIds();
				root.mangleLocalIds( 0 );
		}
	}
}
