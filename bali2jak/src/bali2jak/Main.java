package bali2jak;

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

import Jakarta.util.Util;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import java.lang.reflect.Method;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Properties;

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
    static String packName = "";

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
    protected void argInquire( int _layer ) {

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
    protected boolean driver( ArgList arguments ) {
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

    //**************************************************
    // Methods called by driver().
    //**************************************************
    protected  AstNode createAST( ArgList argObjects ) {
        return ( null );
    }
    protected  AstNode reduceAST( ArgList argObjects,
                      AstNode ast ) {
        return ( ast );
    }
    protected void outputAST( ArgList argObjects,  AstNode ast ) {}

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

    /**
     * A globally available {@link Logger} for debugging output.  See
     * {@link Main#main(String[])} for additional configuration.
     *
     * @layer<bali>
     */
    final public static Logger DEBUG = Logger.getLogger( "debug" ) ;

    final public static
        String LINE_SEPARATOR = System.getProperty( "line.separator" ) ;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    public static void main( String args[] ) {

        // Initial configuration for the debugging {@link Logger}:
        //
        Main.DEBUG.setLevel( Level.WARNING ) ;
        Main.DEBUG.setUseParentHandlers( false ) ;
        Main.DEBUG.addHandler( LogHandler.CONSOLE ) ;
            
        try {
            Main instance = new  Main() ;
            instance.driver( args ) ;
        }
        catch ( Throwable thrown ) {
            thrown.printStackTrace() ;
            System.exit( 1 ) ;
        }
    }

    /**
     * Returns a version string for this tool.  Each layer should call
     * {@link Main#setVersion(String)} in {@link Main#driver(String[])}.
     * The version for a tool is the most recent version of the layers.
     *
     * <p>
     * Version strings are in ISO-8601 date form.  For example,
     * "v2002.08.27" means this code was last modified on August 27, 2002.
     *
     * @layer<bali>
     */
    public static String getVersion() {
        if ( versionString == null || versionString.length() < 1 )
            return "v0000.00.00" ;
        return versionString ;
    }

    public static String setVersion( String version ) {
        if ( version.length() < "v0000.00.00".length() )
            throw new IllegalStateException( "bad version " + version ) ;
        if ( versionString == null || versionString.length() < 1 )
            versionString = version ;
        else
            if ( versionString.compareTo( version ) < 0 )
                versionString = version ;
        return versionString ;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    // Private material:
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Reads a file specified by filename and returns the content as a
     * {@link String}.
     *
     * @layer<bali>
     */
    private static String file2string( String fileName )
    throws IOException {

        Reader reader = new BufferedReader( inputStreamReader( new FileInputStream( new File( fileName ) ) ) ) ;

        Writer writer = new StringWriter( 1024 ) ;

        char[] data = new char [1024] ;
        for ( int size ; ( size = reader.read( data, 0, data.length ) ) >= 0 ; )
            writer.write( data, 0, size ) ;

        writer.flush() ;
        return writer.toString() ;
    }

    /**
     * Constructs an {@link InputStreamReader} on an {@link InputStream},
     * using encoding "ISO-8859-1" in preference to the default.
     *
     * @layer<bali>
     */
    private static InputStreamReader inputStreamReader( InputStream inp )
    throws FileNotFoundException {

        try {
            return new InputStreamReader( inp, "ISO-8859-1" ) ;
        }
        catch ( UnsupportedEncodingException exception ) {
            return new InputStreamReader( inp ) ;
        }
    }

    /**
     * Normalizes a string by removing repeated whitespace characters.
     *
     * @layer<bali>
     */
    private static String normalize( String string ) {

        StringBuffer buffer = new StringBuffer( string.length() ) ;

        StringTokenizer tokenizer = new StringTokenizer( string ) ;
        while ( tokenizer.hasMoreElements() )
            buffer.append( " " ).append( tokenizer.nextToken() ) ;

        return buffer.append( " " ).toString() ;
    }

    /**
     * Converts a {@link BaliParse} tree into a {@link String} by visiting
     * the tree in a depth-first fashion, printing the tokens that form
     * each node (including special "whitespace" tokens).
     *
     * @layer<bali>
     */
    private static String parse2string( BaliParse parse )
    throws IOException {

        StringWriter stringWriter = new StringWriter( 1024 ) ;
        PrintWriter printWriter = new PrintWriter( stringWriter ) ;

        AstProperties props = new  AstProperties();
        props.setProperty( "output", printWriter ) ;
        parse.print( props ) ;
        printWriter.flush() ;

        return stringWriter.toString() ;
    }

    private static String versionString = null ;

    final public List targetRules = Arrays.asList( new String []
        {"BaliParse", "Options", "ParserCode", "Block", "Statements",
         "Statement", "BaliTokenDefinition", "JavacodeProduction",
         "TokenManagerDeclarations", "ScanBlock", "BaliGrammarRule",
         "Productions", "Production", "Lookahead", "Rewrite",
         "PrimitiveRewrite", "Pattern", "ClassName", "Primitive",
         "Terminal", "RegexTokenDefinition", "StateSet", "StatesSpecifier",
         "StatesList", "StateName", "REKind", "CaseFlag", "REList",
         "RegexBlock", "NextState", "Regex", "AngleRegex", "ComplexRegex",
         "Label"} ) ;

    final private static String PROPERTIES = "bali2jak.properties" ;

    

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Given an {@link Object}, returns a {@link String} containing the
     * normalized {@link Class} name for the object.
     *
     * @layer<bali2jak>
     */
    private static String className( Object object ) {
        return
                ( object != null )
		? object.getClass().getName()
		: "null" ;
    }

    /**
     * Processes an input {@link File} (with Bali source code) into a parse
     * tree, then running a {@link $TEqn.Collector} over the tree to gather
     * all necessary data for later code generation.
     *
     * @return {@link $TEqn.Collector} with collected data from parse tree.
     *
     * @layer<bali2jak>
     */
    private  Collector collectSource( File inpFile )
    throws IOException, ParseException {
        Main.DEBUG.entering( "bali2jak.Main", "collectSource", inpFile ) ;

        Reader reader = new BufferedReader( new InputStreamReader( new FileInputStream( inpFile ),
                    "ISO-8859-1" ) ) ;

        Parser parser =  Parser.getInstance( reader ) ;
        BaliParse tree = ( BaliParse ) parser.parseAll() ;
        reader.close() ;

        Collector collector = new  Collector() ;
        collector.dispatch( tree ) ;

        Main.DEBUG.exiting( "bali2jak.Main", "collectSource", collector ) ;
        return collector ;
    }

    /**
     * Generates the object code {@link File} objects (Jak class files for
     * parse tree nodes) from a {@link $TEqn.Collector} argument.
     *
     * @layer<bali2jak>
     */
    public void generateObject( Collector collector )
    throws IOException {
        Main.DEBUG.entering( "bali2jak.Main", "generateObject" ) ;

        collector.setLayer( argLayer ) ;

        // Generate syntax tree node classes for Bali grammar rules:
        //
        collector.generateNonterminals( argDirectory ) ;

        Main.DEBUG.exiting( "bali2jak.Main", "generateObject" ) ;
    }

    public void loadProperties( String source ) {
        Main.DEBUG.entering( "bali2jak.Main", "loadProperties", source ) ;

        // Allow users to rename resource via system properties:
        //
        String resource = System.getProperty( source, source ) ;

        // Load properties from properties resource:
        //
        Properties properties = new Properties() ;
        try {
            properties.load( ClassLoader.getSystemResourceAsStream( resource ) ) ;
            Main.DEBUG.info( "properties loaded from resource " + resource ) ;
        }
        catch ( NullPointerException thrown ) {
            return ; // Resource not found is ok.
        }
        catch ( IOException thrown ) {
            IllegalStateException exception = 
                            new IllegalStateException( "resource error " + resource ) ;
            exception.initCause( thrown ) ;
            throw exception ;
        }

        // Create a new system Properties object containing the current
        // system properties, but using the application Properties as
        // defaults.  This way, properties defined on the command line
        // (which show up as system properties) override application
        // properties defined via relatively static sources such as
        // resources and files.
        //
        Properties newSystem = new Properties( properties ) ;
        newSystem.putAll( System.getProperties() ) ;
        System.setProperties( newSystem ) ;

        Main.DEBUG.exiting( "bali2jak.Main", "loadProperties" ) ;
    }

    /**
     * Processes a {@link List} of {@link String} arguments that specify
     * the input files and output file.
     *
     * @see #usage()
     *
     * @layer<bali2jak>
     */
    private void parseArguments( List args ) throws IOException {

        // Set default values:
        //
        argLayer = null ; // Generated layer name.
        argDebug = Level.OFF ; // How much debugging output.
        argDirectory = null ; // Output directory.
        argSourceFile = null ; // Bali source file.

        for ( ListIterator p = args.listIterator() ; p.hasNext() ; ) {

            String arg = ( String ) p.next() ;
            if ( arg.equals( "-debug" ) ) {
                argDebug = Level.INFO ;
                if ( p.hasNext() ) {
                    String peek = ( String ) p.next() ;
                    if ( peek.charAt( 0 ) != '-' )
                        argDebug = Level.parse( peek.toUpperCase() ) ;
                    else
                        p.previous() ;
                }
                continue ;
            }

            if ( arg.equals( "-layer" ) && p.hasNext() )
                argLayer = parseLayer( ( String ) p.next() ) ;
            else
                if ( arg.equals( "-a" ) && p.hasNext() )
                    argLayer = parseLayer( ( String ) p.next() ) ;
                else
                    if ( arg.equals( "-directory" ) && p.hasNext() )
                        argDirectory = parseDirectory( ( String ) p.next() ) ;
                    else
                        if ( arg.equals( "-d" ) && p.hasNext() )
                            argDirectory = parseDirectory( ( String ) p.next() ) ;
                        else
                            if ( arg.charAt( 0 ) == '-' )
                                throw new IllegalArgumentException( "invalid: " + arg ) ;
                            else
                                argSourceFile = parseSourceFile( arg ) ;
        }

        Main.DEBUG.setLevel( argDebug ) ;

        if ( argSourceFile == null ) {
            String message = "no Bali source file specified" ;
            throw new IllegalArgumentException( message ) ;
        }

        if ( argDirectory == null )
            argDirectory = parseDirectory( "." ) ;

        if ( argLayer == null ) {

            File directory ;
            try {
                directory = argDirectory.getCanonicalFile() ;
            }
            catch ( IOException exception ) {
                directory = argDirectory.getAbsoluteFile() ;
            }

            argLayer = parseLayer( directory.getName() ) ;
        }

        return ;
    }

    private String parseLayer( String layerName ) {

        if ( argLayer != null )
            throw new IllegalArgumentException( "option \"-layer\" appears more than once" ) ;

        if ( ! layerName.matches( "^[\\p{Alpha}_$][\\p{Alnum}_$]*$" ) )
            throw new IllegalArgumentException( "option \"-layer\" doesn't specify an identifier" ) ;

        return layerName ;
    }

    private File parseDirectory( String directoryName ) {

        if ( argDirectory != null )
            throw new IllegalArgumentException( "option \"-directory\" appears more than once" ) ;

        File directory = new File( directoryName ) ;

        if ( ! directory.exists() )
            directory.mkdirs() ;

        if ( ! directory.isDirectory() )
            throw new IllegalArgumentException( "file \""
                            + directory
                            + "\" is not a directory" ) ;

        return directory ;
    }

    private File parseSourceFile( String fileName ) {

        if ( argSourceFile != null )
            throw new IllegalArgumentException( "more than one Bali source file specified" ) ;

        File file = new File( fileName ) ;

        if ( ! file.exists() )
            throw new IllegalArgumentException( "file doesn't exist: "
                            + fileName ) ;

        if ( ! file.canRead() )
            throw new IllegalArgumentException( "file can't be read: "
                            + fileName ) ;

        return file ;
    }

    private void usage( String message ) {

        String program = className( this ) ;

        if ( message != null )
            System.err.println( program + ": " + message ) ;

        System.err.println( "Usage: java "
                + program
                + " [-layer <layer-name>]"
                + " [-directory <output-directory>]"
                + " <Bali-source-file>" ) ;
    }

    private Level argDebug = Level.OFF ; // How many debug messages.
    private String argLayer = null ; // Generated layer name.
    private File argDirectory = null ; // Output directory.
    private File argSourceFile = null ;

    public Object driver( String[] args ) throws Throwable {

        //setVersion( "v2002.08.27" ) ;
    	
    	setVersion( "v2002.09.03" ) ;
		
		Files.setProgram( "bali2jak" ) ;
		Files.setVersion( getVersion() ) ;
		
		loadProperties( PROPERTIES ) ;
		
		try {
		    parseArguments( Arrays.asList( args ) ) ;
		}
		catch ( Exception exception ) {
		    usage( exception.getMessage() ) ;
		    throw exception ;
		}
		
		Main.DEBUG.info( "-layer " + String.valueOf( argLayer ) ) ;
		Main.DEBUG.info( "-directory " + String.valueOf( argDirectory ) ) ;
		Main.DEBUG.info( "source " + String.valueOf( argSourceFile ) ) ;
		
		Collector collector1 = collectSource( argSourceFile ) ;
		generateObject( collector1 ) ;
		
		Main.DEBUG.exiting( "bali2jak.Main", "driver", "collector" ) ;
		Object o = collector1 ;

        Collector collector = (Collector) o;
        
 //               ( Collector ) driver$$bali2jak( args ) ;

        List misDefined = collector.baliRules.misDefined() ;
        Collections.sort( misDefined ) ;
        for ( Iterator p = misDefined.iterator() ; p.hasNext() ; ) {
            String rule = ( String ) p.next() ;
            System.err.println( "rule \"" + rule + "\" is referenced but not defined" ) ;
        }

        return collector ;
    }

}
