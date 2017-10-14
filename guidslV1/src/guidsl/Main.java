package guidsl;

import Jakarta.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.Vector;
import java.io.*;
import java.util.*;
import javax.swing.*;

//**************************************************
// Executing the main of Main will perform the following:
//1) Initialization.
//2) Parse input args and remove switches and their args.
//3) Call the driver() method.
//4) Call the cleanUp() method.
//**************************************************
//
public class Main {

    final static Main instance = new Main();
    static private int layerID_Counter = 0;
    static Vector switches = new Vector();
    static Vector posArgs = new Vector();

    final public static String packageName = Main.getPackageName();

    protected List extraArgs = null;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Methods to manipulate filenames as URIs:
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    public static String file2uri(String fileName) {

        URI fileURI = new File(fileName).toURI().normalize();

        String base = baseURI.getPath();
        String path = fileURI.getPath();
        int minSize = Math.min(base.length(), path.length());

        // Find first position after a slash at which base and path differ:
        //
        int diff = 0;
        while (diff < minSize && base.charAt(diff) == path.charAt(diff)) {
            ++diff;
        }
        diff = 1 + base.lastIndexOf('/', diff);

        // Start a relative URI by first prefixing as many ".." segments
        // as needed to move from base to the common parent prefix:
        //
        StringBuffer uri = new StringBuffer();
        for (int n = diff; (n = 1 + base.indexOf('/', n)) > 0;) {
            uri.append("../");
        }

        // Append the remaining (relative) path that leads to the file:
        //
        uri.append(path.substring(diff));

        return uri.toString();
    }

    public static void setBaseURI(String fileName) {
        if (fileName == null) {
            fileName = ".";
        }
        baseURI = new File(fileName).toURI().normalize();
    }

    public static String uri2file(String uriName) {
        File file = new File(baseURI.resolve(uriName));
        return file.toString();
    }

    private static URI baseURI;

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "modelDirectory" is the base working directory as a File.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    private static File modelDirectory = null;

    /**
     * Returns the base directory as a {@link File} object.
     *
     * @layer<kernel>
     */
    public static File getModelDirectory() {
        return modelDirectory;
    }

    /**
     * Sets the base directory to an absolute {@link File}. If
     * <code>baseName</code> isn't an absolute path, it is resolved relative to
     * the current working directory. If <code>baseName</code> is
     * <code>null</code>, the base directory is set to the current working
     * directory.
     *
     * @layer<kernel>
     */
    public static void setModelDirectory(String baseName) {

        if (baseName == null) {
            baseName = ".";
        }

        modelDirectory = new File(baseName).getAbsoluteFile();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "baseLayer" is derived from the base directory name.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    /**
     * Returns a valid Java identifier that represents the package or layer name
     * for the current file. It is derived from the base directory name, where
     * non-java characters in the "path" are replaced with dots (".")
     *
     * @layer<kernel>
     */
    public static String deriveLayerName() {

        final char DOT = '.';

        // Step 1: determine the relative path to the base directory:
        String base = Util.getFullPath(Main.getModelDirectory());

        File p = new File(kernelConstants.globals().currentAbsPath);
        String path = Util.getFullPath(p.getParentFile());
        if (path.startsWith(base)) {
            path = path.substring(base.length());
        }

        // Step 2: layer name by dropping illegal leading characters,
        // then replacing illegal character sequences with ".":
        StringBuffer layerName = new StringBuffer();

        int index = -1;
        while (++index < path.length()) {
            if (Character.isJavaIdentifierStart(path.charAt(index))) {
                layerName.append(path.charAt(index));
                break;
            }
        }

        boolean haveDot = false;
        while (++index < path.length()) {
            if (Character.isJavaIdentifierPart(path.charAt(index))) {
                layerName.append(path.charAt(index));
                haveDot = false;
            } else if (!haveDot) {
                layerName.append(DOT);
                haveDot = true;
            }
        }

        if (haveDot) {
            return layerName.substring(0, layerName.length() - 1);
        }

        if (layerName.length() < 1) {
            AstNode.error("can't derive layer name");
            return "--unknown--";
        }

        return layerName.toString();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    // Attribute "packageName" is the package name of $TEqn.Main.
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    public static String getPackageName() {
        String pkg = instance.getClass().getName();
        int period = pkg.lastIndexOf('.');
        return (period > 0) ? pkg.substring(0, period) : "";
    }

    //**************************************************
    // main
    //**************************************************
    static String packName = "";

    //**************************************************
    // Parse input args. Remove switches and their args.
    //**************************************************
    protected ArgList parseArgs(String[] args) {
        ArgList argObjects = new ArgList();
        int j, k;
        Switch sw;
        Switch newSwitch;
        String switchName;
        PositionalArg parg;

        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                // switch
                switchName = args[i].substring(1);
                for (j = 0; j < switches.size(); j++) {
                    sw = (Switch) switches.elementAt(j);
                    if (switchName.compareTo(sw.name) == 0) {
                        // Found switch. Clone it.
                        try {
                            newSwitch = (Switch) sw.clone();
                        } catch (CloneNotSupportedException e) {
                            Util.fatalError(e);
                            newSwitch = null;
                        }

                        // Bind args if any
                        if (sw.args != null) {
                            // Allocate array to hold args
                            newSwitch.args = new String[sw.args.length];

                            // Bind args from arg list
                            for (k = 0; k < sw.args.length; k++) {
                                if (++i == args.length) {
                                    usage();
                                }
                                newSwitch.args[k] = args[i];
                            }
                        }

                        // Add newly created Switch object to argObjects.
                        argObjects.addElement(newSwitch);

                        break;
                    }
                } // end of for loop scanning switch list
            } else // non-switch arg
            {
                if (posArgs.size() > 0) {
                    parg = (PositionalArg) posArgs.firstElement();
                    posArgs.removeElementAt(0);
                    parg.binding = args[i];

                    // Add existing PositionalArg object to argObjects.
                    argObjects.addElement(parg);
                } else if (extraArgs != null) {
                    extraArgs.add(args[i]);
                } else {
                    usage();
                }
            }
        }

        // Since we currently do not allow optional positional arguments,
        // make sure all required args have been supplied.
        if (posArgs.size() != 0) {
            usage();
        }

        // Print a usage message if requested:
        //
        if (argObjects.find("help", Switch.class, 0) != null) {
            usage();
        }

        return (argObjects);
    }

    //**************************************************
    // Print out usage of program.
    //**************************************************
    static protected void usage() {
        int i, j;
        Switch sw;
        PositionalArg parg;

        System.err.print("Usage: " + packageName + ".Main");

        // List switches
        for (i = 0; i < switches.size(); i++) {
            sw = (Switch) switches.elementAt(i);
            if (sw.optional) {
                System.err.print(" [");
            } else {
                System.err.print(" ");
            }
            System.err.print("-" + sw.name);
            if (sw.args != null) {
                for (j = 0; j < sw.args.length; j++) {
                    System.err.print(" " + sw.args[j]);
                }
            }
            if (sw.optional) {
                System.err.print("]");
            }
        }

        // List positional arguments
        for (i = 0; i < posArgs.size(); i++) {
            parg = (PositionalArg) posArgs.elementAt(i);
            System.err.print(" <" + parg.name + ">");
        }
        System.err.println();

        // List switch descriptions
        for (i = 0; i < switches.size(); i++) {
            sw = (Switch) switches.elementAt(i);
            System.err.println("\t-" + sw.name + " : " + sw.description);
        }

        // Force exit
        System.exit(1);
    }

    //**************************************************
    // Initialize state prior any other processing.
    //**************************************************
    public void initialize() {
    }

    //**************************************************
    // Must be overridden. Each layer makes zero or more calls to
    // switchRegister() and posArgRegister().  All higher-level layers then
    // call Super(int).argInquire(nextLayer()); (See nextLayer() below.)
    //**************************************************
    //
    protected void argInquire(int _layer) {

        switchRegister(new Switch("base",
                "specifies base working directory",
                new String[]{"<base-working-directory>"},
                true,
                _layer));

        switchRegister(new Switch("a",
                "specifies name of equation file -- .equation(s) are dropped if present",
                new String[]{"<equation-file>"},
                true,
                _layer));

        switchRegister(new Switch("help",
                "prints this helpful usage message",
                null,
                true,
                _layer));

    }

    protected final int nextLayer() {
        return (layerID_Counter++);
    }

    // Services provided by top level. Cannot be overriden.
    protected final void switchRegister(Switch sw) {
        switches.addElement(sw);
    }

    protected final void posArgRegister(PositionalArg parg) {
        posArgs.addElement(parg);
    }

    //**************************************************
    // Can override driver() and call Super().driver() in order to
    // do pre or post processing. The default driver simply calls
    // createAST(), then reduceAST(), then outputAST().
    // returns true if outputAST() is executed, false otherwise.
    // (meaning true if file was translated).
    //**************************************************
    protected boolean driver(ArgList arguments) {
        AstNode ast;

        ast = createAST(arguments);
        if (ast == null) {
            return false;
        }
        ast = reduceAST(arguments, ast);
        if (ast == null) {
            return false;
        }
        outputAST(arguments, ast);
        return true;
    }

    //**************************************************
    // Methods called by driver().
    //**************************************************
    protected AstNode createAST(ArgList argObjects) {
        return (null);
    }

    protected AstNode reduceAST(ArgList argObjects,
            AstNode ast) {
        return (ast);
    }

    protected void outputAST(ArgList argObjects, AstNode ast) {
    }

    protected void cleanUp() {
        // if we get to this point, there have been no fatal errors
        // but there may have been errors, and their numbers may have
        // accumulated if we have processed multiple files.  If
        // there are any errors at this time, then exit with an error
        // indicator (so that composer knows something went wrong).

        int nerrors = AstNode.errorCount();
        if (nerrors != 0) {
            System.exit(1);
        }
    }
    static boolean debug = false;
    static boolean printFile = false;
    static boolean dumpPtable = false;
    static boolean dumpVtable = false;
    public static boolean modelMode = false;
    static String inputFileName = "";
    static final String defaultModelFile = "model.m";

    static void marquee() {
        System.out.println("Usage:  guidsl <options> <file>.m");
        System.out.println("        <file>.m is a feature model");
        System.out.println("        options -d debug");
        System.out.println("                -p print input file");
        System.out.println("                -m model mode uses 'model.m'");
        Main.marqueeAdditions();
        System.exit(1);
    }

    // the following methods are extension points for the Main class
    static final void marqueeAdditions$$dsl$guidsl$dmain() {
        // layers extend this method with their option descriptions
    }

    static final boolean processOptions$$dsl$guidsl$dmain(char o) {
        // layers extend this method with option processing
        // default returns false, but this should be overridden
        return false;
    }

    static final void debugActions$$dsl$guidsl$dmain() {
        // layers extend this method with debug reporting
    }

    public static final void process$$dsl$guidsl$dmain(Model root) throws SemanticException {
        // layers extend this method for AST processing
    }

    public static final void process2$$dsl$guidsl$dmain() {
        // layers extend this method for gspec object procssing
    }

    public static final void main$$dsl$guidsl$dmain(String args[]) {
        int argc = args.length;
        int non_switch_args;

        // Step 1: a general routine to pick off command line options
        //         options are removed from command line and
        //         args array is adjusted accordingly.
        //         right now, there are no command-line options
        //         but this code is here for future expansion
        non_switch_args = 0;
        for (int i = 0; i < argc; i++) {
            if (args[i].charAt(0) == '-') {

                // switches of form -xxxxx (where xxx is a sequence of 1
                // or more characters
                for (int j = 1; j < args[i].length(); j++) {
                    char o = args[i].charAt(j);
                    if (Main.processOptions(o)) {
                        continue;
                    }
                    if (o == 'd') {
                        debug = true;
                        continue;
                    }
                    if (o == 'p') {
                        printFile = true;
                        continue;
                    }
                    if (o == 'm') {
                        modelMode = true;
                        continue;
                    }
                    System.err.println("Unrecognizable option " + o);
                    Main.marquee();

                    // if (args[i].charAt(j) == 'x' {
                    //        ... do this for option 'x'
                    // }
                }
            } else {
                // non-switch arg

                args[non_switch_args] = args[i];
                non_switch_args++;
            }
        }

        // assume "model.m" as the default name of a file.
        // we use this default if non_switch_args == 0 and
        // there is no such "model.m" file in the current directory
        if (non_switch_args == 0) {
            if (modelMode) {
                inputFileName = defaultModelFile;
            } else {
                Main.marquee();
            }
        } else {
            inputFileName = args[0];
        }

        // Step 2: open file
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(inputFileName);
        } catch (Exception e) {
            System.err.println("File " + inputFileName + " not found:"
                    + e.getMessage());
            Main.marquee();
        }

        // Step 3: create a parser and parse input files
        //         inputRoot is root of parse tree of input file
        Parser myParser = Parser.getInstance(inputFile);
        Model inputRoot = null;
        try {
            inputRoot = (Model) myParser.parseAll();
        } catch (Exception e) {
            System.out.println("Parsing Exception Thrown in "
                    + inputFileName + ": " + e.getMessage());
            System.exit(1);
        }

        // Step 4: Initialize output stream to standard out
        //         Standard initialization stuff that should be
        //         platform independent.
        PrintWriter pw = null;

        AstProperties props = new AstProperties();
        String lineSeparator
                = System.getProperties().getProperty("line.separator");

        if (lineSeparator.compareTo("\n") != 0) {
            pw = new PrintWriter(new FixDosOutputStream(System.out));
        } else {
            pw = new PrintWriter(System.out);
        }

        props.setProperty("output", pw);

        // Step 5: transform parse tree here
        try {
            Main.process(inputRoot);
        } catch (SemanticException e) {
            int errorCnt = Util.errorCount();
            System.err.println(Util.errorCount() + " error(s) found");
            System.err.println("Processing terminated");
            System.exit(1);
        }

        Main.process2();

        if (printFile) {
            inputRoot.print();
            System.out.println();
        }
        if (debug) {
            Main.debugActions();
        }
    }
    // dump key tables

    static final void debugActions$$dsl$guidsl$gspec() {
        variable.dumpVtable();
        production.dumpPtable();
        pattern.dumpTtable();
        debugActions$$dsl$guidsl$dmain();
    }

    static public final void process$$dsl$guidsl$fillgs(Model m) throws SemanticException {
        process$$dsl$guidsl$dmain(m);
        // harvest the tree
        m.harvest(new fillFPtable());
        if (Util.errorCount() != 0) {
            throw new SemanticException("Error(s) in specification found");
        }
        m.harvest(new enterGspec());
        if (Util.errorCount() != 0) {
            throw new SemanticException("Error(s) in specification found");
        }
    }

    static final void debugActions$$dsl$guidsl$eharvest() {
        ESList.dumpCTable();
        debugActions$$dsl$guidsl$gspec();
    }

    static public final void process$$dsl$guidsl$propgs(Model m) throws SemanticException {
        process$$dsl$guidsl$fillgs(m);
        grammar.current.visit(new propcons());
        if (Util.errorCount() != 0) {
            throw new SemanticException("Errors in propagating Constraints");
        }
    }

    static final void debugActions$$dsl$guidsl$printgs() {
        grammar.current.visit(new print());
        debugActions$$dsl$guidsl$eharvest();
    }

    static public final void process$$dsl$guidsl$formgs(Model m) throws SemanticException {
        process$$dsl$guidsl$propgs(m);
        production.makeFormula();
        pattern.makeFormula();
        if (Util.errorCount() != 0) {
            throw new SemanticException("Errors in making propositional formulas");
        }
    }
    static boolean outputModelPredicate = false;

    static final boolean processOptions$$dsl$guidsl$clauselist(char o) {
        if (processOptions$$dsl$guidsl$dmain(o)) {
            return true;
        }
        if (o == 'o') {
            outputModelPredicate = true;
        }
        return (o == 'o');
    }

    static final void marqueeAdditions$$dsl$guidsl$clauselist() {
        System.out.println("                -o output model predicate");
        marqueeAdditions$$dsl$guidsl$dmain();
    }

    static public final void process$$dsl$guidsl$clauselist(Model m) throws SemanticException {
        process$$dsl$guidsl$formgs(m);
        production.makeClauses();
        pattern.makeClauses();
        ESList.makeClauses();
        grammar.makeClauses();
        if (Util.errorCount() != 0) {
            throw new SemanticException("Errors in making conjunctive normal formulas");
        }
        // now exit if we set the -o option
        if (outputModelPredicate) {
            System.exit(0);
        }
    }
    static Gui guiObj;

    static public void process2() {
        process2$$dsl$guidsl$dmain();
        variable.clearUserVisible();
        guiObj = new Gui(grammar.rootProduction.name);
    }
    static boolean equationFormat = false;

    static final void marqueeAdditions$$dsl$guidsl$output() {
        marqueeAdditions$$dsl$guidsl$clauselist();
        System.out.println("                -e equation file format");
    }

    static final boolean processOptions$$dsl$guidsl$output(char o) {
        if (o == 'e') {
            equationFormat = true;
            Gui.equations = "equation";
            return true;
        }
        return processOptions$$dsl$guidsl$clauselist(o);
    }

    static void setEquationFormat(boolean v) {
        equationFormat = v;
        if (v) {
            Gui.equations = "equation";
        } else {
            Gui.equations = "equations";
        }
    }
    // define a flag "CheckForInconsistencies" that is set via
    // command-line invocation

    // add -c option to marquee, and processOptions
    // take action in extending Main.main()
    static boolean CheckForInconsistencies = false;

    static void marqueeAdditions() {
        System.out.println("                -c check for inconsistencies");
        marqueeAdditions$$dsl$guidsl$output();
    }

    static boolean processOptions(char o) {
        if (o == 'c') {
            CheckForInconsistencies = true;
            return true;
        }
        return processOptions$$dsl$guidsl$output(o);
    }

    public static void main(String args[]) {
        main$$dsl$guidsl$dmain(args);
        if (CheckForInconsistencies) {
            guiObj.setVisible(false);
            solverTest.checkModel();
            // perform analysis here;
            System.exit(0);
        }
    }

    static char buffer[] = new char[1000];

    static public void process(Model m) throws SemanticException {
        process$$dsl$guidsl$clauselist(m);

        if (modelMode) {
            try {
                harvestInfo();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Model Harvesting Error -- see command line for details",
                        "Error!", JOptionPane.ERROR_MESSAGE);
                System.err.println(e.getMessage());
            }
        }
    }

    File root = null;
    File par = null;
    final static String modelExtension = ".m";
    final static String helpfile = "help.html";
    final static String featureExpl = "feature.expl";

    static void harvestInfo() throws IOException {
        // Step 1: get par directory

        File root = new File(".");
        File par = new File(root.getAbsolutePath()).getParentFile();

        // Step 2: create file filter that retrieves
        //         only the subdirectories in this directory
        FileFilter ff = new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };

        // Step 3: read all the subdirectories (layers) in this model
        File[] list = par.listFiles(ff);
        for (int i = 0; i < list.length; i++) {
            String layerName = list[i].getName();
            variable v = variable.find(layerName);

            if (v != null) {
                if (v.type != variable.Prim && Main.debug) {
                    Util.error("feature " + v.name + " is not a primitive in this directory");
                }

                String hname = layerName + File.separator + helpfile;
                File html = new File(hname);
                if (html.isFile()) {
                    v.helpfile = hname;
                }
                File help = new File(layerName + File.separator + featureExpl);
                if (help.isFile()) {
                    // this is a hack -- feature files are at most 1000 chars long
                    FileReader fr = new FileReader(help);
                    fr.read(buffer);
                    v.help = new String(buffer);
                }
            }
        }

        // Step 4: make sure that all terminals in the grammar are actually layers
        if (!Main.debug) {
            return;
        }
        Iterator i = variable.Vtable.values().iterator();
        while (i.hasNext()) {
            variable v = (variable) i.next();
            if (v.type != variable.Prim) {
                continue;
            }
            File testname = new File(v.name);
            if (!testname.isDirectory()) {
                Util.error(v.name + " is a grammar primitive but not implemented ");
            }
        }
    }

    static void debugActions() {
        grammar.current.visit(new order());
        debugActions$$dsl$guidsl$printgs();
    }

    //Gets gramamr and constraints as XML and joins them up
    // This is the interface that external applicataions will use
    public static String getModelXML() {
        PrintXML printer = new PrintXML();

        String grammar = printer.getXMLString();
        String constraints = ESList.getCTableXML();

        return XMLUtils.formatXMLStr("<model>" + grammar + constraints + "</model>");
    }

}
