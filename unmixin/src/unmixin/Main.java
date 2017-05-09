package unmixin;

import Jakarta.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Vector;

import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

import java.util.*;
import java.io.*;

//**************************************************
// Executing the main of Main will perform the following:
//1) Initialization.
//2) Parse input args and remove switches and their args.
//3) Call the driver() method.
//4) Call the cleanUp() method.
//**************************************************
//
    
public class Main {

    final static  Main instance = new  Main();
    static private int layerID_Counter = 0;
    static Vector switches = new Vector();
    static Vector posArgs = new Vector();

    final public static String packageName =  Main.getPackageName() ;

    protected List extraArgs = null ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Methods to manipulate filenames as URIs:
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    public static String file2uri( String fileName ) {

        URI fileURI = new File( fileName ) . toURI() . normalize() ;

        String base = baseURI.getPath() ;
        String path = fileURI.getPath() ;
        int minSize = Math.min( base.length(), path.length() ) ;
            
        // Find first position after a slash at which base and path differ:
        //
        int diff = 0 ;
        while ( diff < minSize && base.charAt( diff ) == path.charAt( diff ) )
            ++ diff ;
        diff = 1 + base.lastIndexOf( '/', diff ) ;

        // Start a relative URI by first prefixing as many ".." segments
        // as needed to move from base to the common parent prefix:
        //
        StringBuffer uri = new StringBuffer() ;
        for ( int n = diff ; ( n = 1 + base.indexOf( '/', n ) ) > 0 ; )
            uri.append( "../" ) ;

        // Append the remaining (relative) path that leads to the file:
        //
        uri.append( path.substring( diff ) ) ;

        return uri.toString() ;
    }

    public static void setBaseURI( String fileName ) {
        if ( fileName == null )
            fileName = "." ;
        baseURI = new File( fileName ) . toURI() . normalize() ;
    }

    public static String uri2file( String uriName ) {
        File file = new File( baseURI.resolve( uriName ) ) ;
        return file.toString() ;
    }

    private static URI baseURI ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "modelDirectory" is the base working directory as a File.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    private static File modelDirectory = null ;

    /**
     * Returns the base directory as a {@link File} object.
     *
     * @layer<kernel>
     */
    public static File getModelDirectory() {
        return modelDirectory ;
    }

    /**
     * Sets the base directory to an absolute {@link File}.  If
     * <code>baseName</code> isn't an absolute path, it is resolved
     * relative to the current working directory.  If <code>baseName</code>
     * is <code>null</code>, the base directory is set to the current
     * working directory.
     *
     * @layer<kernel>
     */
    public static void setModelDirectory( String baseName ) {

        if ( baseName == null )
            baseName = "." ;

        modelDirectory = new File( baseName ) . getAbsoluteFile() ;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "baseLayer" is derived from the base directory name.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    /**
     * Returns a valid Java identifier that represents the package or
     * layer name for the current file.  It is derived from the base
     * directory name, where non-java characters in the "path" are
     * replaced with dots (".")
     *
     * @layer<kernel>
     */
    public static String deriveLayerName() {

        final char DOT = '.' ;

        // Step 1: determine the relative path to the base directory:

        String base = Util.getFullPath( Main.getModelDirectory() ) ;

        File p = new File( kernelConstants.globals().currentAbsPath );
        String path = Util.getFullPath( p.getParentFile() ) ;
        if ( path.startsWith( base ) )
            path = path.substring( base.length() ) ;

        // Step 2: layer name by dropping illegal leading characters,
        // then replacing illegal character sequences with ".":

        StringBuffer layerName = new StringBuffer() ;

        int index = -1 ;
        while ( ++index < path.length() )
            if ( Character.isJavaIdentifierStart( path.charAt( index ) ) ) {
                layerName.append( path.charAt( index ) ) ;
                break ;
            }

        boolean haveDot = false ;
        while ( ++index < path.length() )
            if ( Character.isJavaIdentifierPart( path.charAt( index ) ) ) {
                layerName.append( path.charAt( index ) ) ;
                haveDot = false ;
            }
            else
                if ( ! haveDot ) {
                    layerName.append( DOT ) ;
                    haveDot = true ;
                }

        if ( haveDot )
            return layerName.substring( 0, layerName.length()-1 ) ;

        if ( layerName.length() < 1 ) {
            AstNode.error( "can't derive layer name" ) ;
            return "--unknown--" ;
        }

        return layerName.toString() ;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "packageName" is the package name of $TEqn.Main.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    public static String getPackageName() {
        String pkg = instance.getClass().getName() ;
        int period = pkg.lastIndexOf( '.' ) ;
        return ( period > 0 )	? pkg.substring( 0, period ) : "" ;
    }

    //**************************************************
    // main
    //**************************************************
    static String packName = ""; // assigned below.  This is the
    // name of the equation file, minus the
    // .equation or .equations ending
    // this may be reset with a default
    // in the AspectTrans layer

    static public void main( String[] args ) {
        ArgList arguments;

        instance.initialize();

        // Request layers to register their interests in switches
        // and positional arguments.
        instance.argInquire( 0 );

        // Parse input args. Remove switches and their args.
        arguments = instance.parseArgs( args );

        // Extract arguments for this layer:
        //
        // -base specifies base directory (defaults to current directory)
        //
        Switch dirSwitch =
                ( Switch ) arguments.find( "base",  Switch.class, 0 ) ;

        Main.setModelDirectory( ( dirSwitch != null ) ? dirSwitch.args[0] : null ) ;

        Switch packSwitch =
                ( Switch ) arguments.find( "a",  Switch.class, 0 ) ;
        String tmp = ( ( packSwitch != null ) ? packSwitch.args[0] : "" );
        if ( tmp.endsWith( ".equation" ) )
            tmp = tmp.substring( 0, tmp.length() - 9 );
        else
            if ( tmp.endsWith( ".equations" ) )
                tmp = tmp.substring( 0, tmp.length() - 10 );
        packName = tmp;

        // reset error counter -- the counter is examined
        // in cleanUp after all file arguments have been processed

        Util.resetCounters();

        // Call driver()
        instance.driver( arguments );

        // Clean up.
        instance.cleanUp();
    }

    //**************************************************
    // Parse input args. Remove switches and their args.
    //**************************************************
    protected ArgList parseArgs( String[] args ) {
        ArgList argObjects = new ArgList();
        int j,k;
        Switch sw;
        Switch newSwitch;
        String switchName;
        PositionalArg parg;

        for ( int i=0; i < args.length; i++ ) {
            if ( args[i].charAt( 0 ) == '-' ) {
                // switch
                switchName = args[i].substring( 1 );
                for ( j=0; j < switches.size(); j++ ) {
                    sw = ( Switch ) switches.elementAt( j );
                    if ( switchName.compareTo( sw.name ) == 0 ) {
                        // Found switch. Clone it.
                        try {
                            newSwitch = ( Switch ) sw.clone();
                        }
                        catch ( CloneNotSupportedException e ) {
                            Util.fatalError( e );
                            newSwitch = null;
                        }

                        // Bind args if any
                        if ( sw.args != null ) {
                            // Allocate array to hold args
                            newSwitch.args = new String[sw.args.length];

                            // Bind args from arg list
                            for ( k=0; k < sw.args.length; k++ ) {
                                if ( ++i == args.length )
                                    usage();
                                newSwitch.args[k] = args[i];
                            }
                        }

                        // Add newly created Switch object to argObjects.
                        argObjects.addElement( newSwitch );

                        break;
                    }
                } // end of for loop scanning switch list
            }
            else {
                // non-switch arg
                if ( posArgs.size() > 0 ) {
                    parg = ( PositionalArg ) posArgs.firstElement();
                    posArgs.removeElementAt( 0 );
                    parg.binding = args[i];

                    // Add existing PositionalArg object to argObjects.
                    argObjects.addElement( parg );
                }
                else
                    if ( extraArgs != null )
                        extraArgs.add( args [i] ) ;
                    else
                        usage() ;
            }
        }

        // Since we currently do not allow optional positional arguments,
        // make sure all required args have been supplied.
        if ( posArgs.size() != 0 )
            usage();

        // Print a usage message if requested:
        //
        if ( argObjects.find( "help",  Switch.class, 0 ) != null )
            usage() ;

        return ( argObjects );
    }

    //**************************************************
    // Print out usage of program.
    //**************************************************
    static protected void usage() {
        int i, j;
        Switch sw;
        PositionalArg parg;

        System.err.print( "Usage: " + packageName + ".Main" ) ;

        // List switches
        for ( i=0; i < switches.size(); i++ ) {
            sw = ( Switch ) switches.elementAt( i );
            if ( sw.optional )
                System.err.print( " [" );
            else
                System.err.print( " " );
            System.err.print( "-" + sw.name );
            if ( sw.args != null ) {
                for ( j=0; j < sw.args.length; j++ )
                    System.err.print( " " + sw.args[j] );
            }
            if ( sw.optional )
                System.err.print( "]" );
        }

        // List positional arguments
        for ( i=0; i < posArgs.size(); i++ ) {
            parg = ( PositionalArg ) posArgs.elementAt( i );
            System.err.print( " <" + parg.name + ">" );
        }
        System.err.println();

        // List switch descriptions
        for ( i=0; i < switches.size(); i++ ) {
            sw = ( Switch ) switches.elementAt( i );
            System.err.println( "\t-" + sw.name + " : " + sw.description );
        }

        // Force exit
        System.exit( 1 ) ;
    }

    //**************************************************
    // Initialize state prior any other processing.
    //**************************************************
    public void initialize() {}

    //**************************************************
    // Must be overridden. Each layer makes zero or more calls to
    // switchRegister() and posArgRegister().  All higher-level layers then
    // call Super(int).argInquire(nextLayer()); (See nextLayer() below.)
    //**************************************************
    //
    protected final void argInquire$$kernel( int _layer ) {

        switchRegister( new Switch( "base",
                    "specifies base working directory",
                    new String[] {"<base-working-directory>"},
                    true,
                    _layer ) ) ;

        switchRegister( new Switch( "a",
                    "specifies name of equation file -- .equation(s) are dropped if present",
                    new String[] {"<equation-file>"},
                    true,
                    _layer ) ) ;

        switchRegister( new Switch( "help",
                    "prints this helpful usage message",
                    null,
                    true,
                    _layer ) ) ;

    }

    protected final int nextLayer() {
        return ( layerID_Counter++ );
    }

    // Services provided by top level. Cannot be overriden.
    protected final void switchRegister( Switch sw ) {
        switches.addElement( sw );
    }
    protected final void posArgRegister( PositionalArg parg ) {
        posArgs.addElement( parg );
    }

    //**************************************************
    // Can override driver() and call Super().driver() in order to
    // do pre or post processing. The default driver simply calls
    // createAST(), then reduceAST(), then outputAST().
    // returns true if outputAST() is executed, false otherwise.
    // (meaning true if file was translated).
    //**************************************************
    protected final boolean driver$$kernel( ArgList arguments ) {
        AstNode ast;

        ast = createAST( arguments );
        if ( ast == null )
            return false;
        ast = reduceAST( arguments, ast );
        if ( ast == null )
            return false;
        outputAST( arguments, ast );
        return true;
    }
    protected  AstNode reduceAST( ArgList argObjects,
                      AstNode ast ) {
        return ( ast );
    }
    protected final void outputAST$$kernel( ArgList argObjects,  AstNode ast ) {}

    protected void cleanUp() {
        // if we get to this point, there have been no fatal errors
        // but there may have been errors, and their numbers may have
        // accumulated if we have processed multiple files.  If
        // there are any errors at this time, then exit with an error
        // indicator (so that composer knows something went wrong).

        int nerrors =  AstNode.errorCount();
        if ( nerrors != 0 )
            System.exit( 1 );
    }

    final private static String SOURCE_FILE = "source file" ;

    private ArgList instanceArgs = null ;
    private PositionalArg sourceFile = null ;

    private int myLayerID;
    public static boolean verbose = false;

    private static HashSet filesProcessed = new HashSet();
    public static String currentOutputFileName ="";

    // given absolute path of file, directoryPath returns
    // the absolute path of the file's directory

    String directoryPath( String path ) {
        if ( path == null )
            return ".";
        int lst = path.lastIndexOf( File.separatorChar );
        if ( lst == -1 )
            return ".";
        else
            return path.substring( 0, lst );
    }

    /**
     * Overrides previous version to allow multiple source files in the
     * argument list.  This is done by successively substituting the
     * "source file" argument by each of the extra arguments.
     *
     * <p>
     * <em>Note:</em>
     * The argument processing here is a mess, mostly because the command
     * line parsing design is poor.  That's another area of clean-up.
     *
     * @layer<Java>
     */
    protected boolean driver( ArgList args ) {

        instanceArgs = args ;

        // Find the "source file" argument to be updated.
        //
        for ( Iterator p = args.iterator() ; p.hasNext() ; ) {
            Object object = p.next() ;
            if ( object instanceof PositionalArg ) {
                PositionalArg arg = ( PositionalArg ) object ;
                if ( SOURCE_FILE.equals( arg.name ) ) {
                    sourceFile = arg ;
                    break ;
                }
            }
        }

        if ( sourceFile == null )
            throw new IllegalStateException( "invalid source file parse" ) ;

        // Get verbosity level:
        //
        for ( Iterator p = args.iterator() ; p.hasNext() ; ) {
            Object object = p.next() ;
            if ( object instanceof Switch ) {
                Switch sw = ( Switch ) object ;
                if ( sw.name == "quiet" )
                    verbose = false ;
                else
                    if ( sw.name == "verbose" )
                        verbose = true ;
            }
        }

        // Handle first source file argument (at least one is required):
        //
        processing( packageName, sourceFile.binding ) ;

        // If there are no extra arguments, then we're done.
        //
        if ( extraArgs == null || extraArgs.size() < 1 )
            return true;

        // Substitute each extra source file argument into the source file
        // position, then re-evaluate command line.
        //
        for ( Iterator p = extraArgs.iterator() ; p.hasNext() ; )
            processing( packageName, ( String ) p.next() ) ;

        return true;
    }

    protected final void processing$$Java( String label, String fileName ) {

        sourceFile.binding = fileName ;
        kernelConstants.globals().currentFileName = fileName;
        boolean processed = driver$$kernel( instanceArgs );

        if ( verbose && processed )
            System.err.println( label
                            + ": file \""
                            + fileName
                            + '"' ) ;
    }

    //**************************************************
    // Method called by the top-most layer to allow a layer to request
    // switches and arguments.
    //**************************************************
    protected void argInquire( int _layer ) {
        Switch sw;

        // Save my layer number
        myLayerID = _layer;

        // Register my switches
        sw = new Switch( "d", "debug mode for parser", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "s", "send output to stdout", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "b", "bootstrap from JTS to FOP", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "v", "FOP (exit(1)) or JTS (exit(0)) version", null,  true, _layer );
        switchRegister( sw );
        sw = new Switch( "x", "override default file extension", new String[1],  true, _layer );
        switchRegister( sw );

        // Verbosity selection:
        //
        switchRegister( new Switch( "quiet",
                    "disables verbose output",
                    null,
                    true,
                    _layer ) ) ;

        switchRegister( new Switch( "verbose",
                    "enables verbose output (default)",
                    null,
                    true,
                    _layer ) ) ;

        // Register my command line positional arguments
        posArgRegister( new PositionalArg( SOURCE_FILE, _layer ) ) ;

        // Allow extra arguments:
        extraArgs = new ArrayList() ;

        // Call next layer
        argInquire$$kernel( nextLayer() );
    }

    //**************************************************
    // createAST()
    //**************************************************
    protected  AstNode createAST( ArgList argObjects ) {
        FileInputStream fis;
        PositionalArg parg;
        File inputFile;
        AstNode root;

        // do no file processing if -v switch is set;
        // just return version

        if ( argObjects.find( "v", Switch.class, myLayerID ) != null ) {
            if ( kernelConstants.LangName.equals( "" ) )
                System.exit( 1 );
            else
                System.exit( 0 );
        }

        Switch sw = ( Switch ) argObjects.find( "x", Switch.class, myLayerID );
        if ( sw != null ) {
            kernelConstants.jakExtension = sw.args[0];
        }
        parg = ( PositionalArg ) argObjects.first( PositionalArg.class, myLayerID );

        kernelConstants.PushParseTreeStack( parg.binding );
        try {
            inputFile = new File( parg.binding );
            fis = new FileInputStream( inputFile );
            kernelConstants.globals().mainProps.setProperty( "input", inputFile );

            // now add inputDirectory property
            String abspath = inputFile.getAbsolutePath();
            kernelConstants.globals().mainProps.setProperty( "inputDirectory",
                              directoryPath( abspath ) );
            kernelConstants.globals().currentAbsPath = abspath;

            // extract file extension -- if there is none, use ""
            String x = "";
            int i = parg.binding.lastIndexOf( '.' );
            if ( i != -1 )
                x = parg.binding.substring( i );
            kernelConstants.globals().currentFileExt = x;

            // see if we have already processed this file.  If so, return null

            if ( filesProcessed.contains( abspath ) ) {
                fis.close();
                kernelConstants.PopParseTreeStack();
                return null;
            }
        }
        catch ( Exception e ) {
            AstNode.fatalError( "Can't open file "+ parg.binding );
            fis = null;
        }

        try {
            Parser parser =  Parser.getInstance( fis ) ;
            root = parser.parseAll() ;
        }
        catch ( ParseException e ) {
            AstNode.parseError( e.toString() );
            root = null;
        }

        return ( root );
    }
    protected void processing( String label, String fileName ) {
        setBaseURI( new File( fileName ). getAbsoluteFile() . getParent() ) ;
        processing$$Java( label, fileName );
    }

    protected void outputAST( ArgList argObjects,  AstNode ast ) {
        // this method does the work of UnMixin -- overrides
                  // standard outputAST method and replaces it with this one.
             //
                        
                  // Step 0: convert ast into AST_Program

        AST_Program p = ( AST_Program ) ast;

             // Step 1: unmangle the mangled identifiers
        //
        p.unmangleIds( 0 );
   
        // Step 2: for each SoUrCe/Type declaration pair
        //         propagate chanes back to the original file
        p.propagateChanges();
    }

}
