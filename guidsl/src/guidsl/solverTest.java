package guidsl;// SAT Solver Test (SST)
//

import java.io.*;
import java.util.*;
import java.util.ArrayList;


// accepts file as input.
// if satisfiable, then the solutions are printed

public class solverTest {
   static final String input2SATSolver = "_debug.cnf";
    static int[] combArr = null;
    static int pivot = -1;

    // this method does double-duty.
    // if 2 command-line arguments are given, the first file input
    // is assumed to be a #true-#end test
    // if only 1 file input is given, it is assumed to be a .cnf file

    public static void main( String args[] ) {
        if ( args.length > 1 ) {
            try {
                modelDebug( args[0] , true );
            }
            catch ( Exception e ) {
                outln( "Exception in processing " + args[0] + " " +
                                                                                             e.getMessage() );
                outln( "Processing of " + args[0] + " aborted" );
            }
            return;
        }
        if (args.length !=1 ) {
		     System.err.println(" must supply a .cnf file command-line argument");
		     System.exit(1);
		  }
		  SATSolver s = new SATSolver();
		  boolean result = s.solve(args[0]);
		  System.out.println( args[0] + " " + result );
		  s.decode();
    }

/********************* Function creates a _debug.cnf file, an input for the Model Checker SPIN*****************/
    static void createCNF()
     throws dparseException, IOException {
        boolean saveInFile = true;
        String filename = "Test.txt";
        boolean result;
        variable var = null;
        FileOutputStream fOutputStream = new FileOutputStream(filename);
        PrintStream outTest = new PrintStream(fOutputStream);
        outTest.println("#true");
        outTest.println("#end");
        outTest.close();
        fOutputStream.close();

        // Step 0 -- for debugging cnf files
                  // variable.dumpVariablesInOrder();

        // Step 1: create model cnf file string
        //
        cnfModel model = cnfModel.init();
        boolean testSucceeded = true;

        outlnModelCheck("Creating _debug.cnf...");
        outlnModelCheck("\tStarting SatSolver...");
        outlnModelCheck( "\t\tBeginning test : " + filename );
        outlnModelCheck();

        dparser d = new dparser( filename );
        while ( true ) {
            SATtest t = d.getNextTest();
            if ( t == null )
                break;

            if (t.isComplete()) {
                   // temporarily remove UserSelections
                    ArrayList orig = grammar.UserSelections;
                    grammar.UserSelections = new ArrayList();

               // now perform the complete test
                    // convert strings into variable references
                    grammar.UserSelections.clear();
                    Vector v = t.getSelections();
                    for (int i=0; i<v.size(); i++) {
                       var = (variable) (var.Vtable.get(v.get(i)));
                        grammar.UserSelections.add(var);
                    }
                    grammar.propagate();
                    testSucceeded = reportResult( t.getName(),
                                    cnfClause.complete(false) == t.isSat, testSucceeded);

                    // replace original UserSelections
                    grammar.UserSelections = orig;
                    continue;
                }

            // create the file to be tested, invoke the solver
            // and output the result

            SATSolver s = new SATSolver();
            if ( saveInFile ) {
                createOutputFile( model, t );
                result = s.solve( input2SATSolver );
            }
            else { // use in-memory file
                createOutputBuffer( model, t );
                result = s.solve( new LineNumberReader( new StringReader( cnfFileString ) ) );
            }

            testSucceeded = reportResultModelCheck( t.getName(),
                                result == t.isSat, testSucceeded );
        }
        outlnModelCheck();
        outModelCheck( "\t\tSummary of " + filename + " test : " );
        if ( testSucceeded )
            outlnModelCheck( " ALL SUCCEEDED" );
        else
            outlnModelCheck( " SOME FAILED" );
    }

/********** Promela Translator *************/
static boolean promelaGenerate(String cnfFile){
        String header;
        String threeValTypeDef = "mtype = {T, F, U};";
        String startInitBlk = "init\n{";
        ArrayList programVars;
        ArrayList visibleFeatureVars;
        ArrayList clauseCollection;
        ArrayList contradiction;

        programVars = new ArrayList();
        visibleFeatureVars = new ArrayList();
        contradiction = new ArrayList();
        clauseCollection = new ArrayList();

        boolean debug = false;
        FileInputStream fInputStream;
        FileOutputStream fOutputStream;
        DataInputStream in;
        PrintStream outPG;
        String[] pLine;

            try {
                fInputStream = new FileInputStream(cnfFile);
                fOutputStream = new FileOutputStream("_debug.pml");
                in = new DataInputStream(fInputStream);
                outPG = new PrintStream(fOutputStream);

                header =
                    "/************************************************************************/\n" +
                    "/*                        Promela code file                             */\n" +
                    "/*                               By                                     */\n" +
                    "/*                        Adithya Hemakumar                             */\n" +
                    "/*                 The University of Texas at Austin                    */\n" +
                    "/*                                                                      */\n" +
                    "/*                         Supervised by                                */\n" +
                    "/*                         Dr. Don Batory                               */\n" +
                    "/*                 The University of Texas at Austin                    */\n" +
                    "/************************************************************************/\n";
                outlnModelCheck("Parsing _debug.cnf...");
                while(in.available() != 0){
                    String fileLine = in.readLine();
                    pLine = fileLine.split("\\s+");
                    if(debug){
                        for(int i = 0; i< pLine.length; i++){
                            System.out.println(pLine[i]);
                        }
                    }
                    if(pLine.length == 1) continue;
                    switch (pLine[0].charAt(0)){
                    case 'p':
                        break;
                    case 'c':
                        if(pLine[1].charAt(0) == 'u'){
                            programVars.add(Integer.parseInt(pLine[2])-1, pLine[3]);
                            visibleFeatureVars.add(pLine[3]);
                        }
                        else if(pLine[1].charAt(0) == 'c') programVars.add(Integer.parseInt(pLine[2])-1, pLine[3]);
                        break;
                    default:
                        if((pLine[0].charAt(0) != 'c') && (pLine[0].charAt(0) != 'p') && (pLine.length > 2)){
                            ArrayList clause = new ArrayList();
                            String inconsistencyCheck = "(!(";


                            /********* for each predicate do the following ***********/
                            for (int i = 0; i < pLine.length-1; i++){
                                int vIndex = Integer.parseInt(pLine[i]);
                                int vIndexCopy = vIndex;
                                if(vIndexCopy < 0) vIndex += 1;
                                else vIndex += -1;

                                String term = "(" + (String)programVars.get(Math.abs(vIndex)) + " == U && ";


                                /******* for each term in the predicate do the following *********/
                                for(int j = 0; j < pLine.length-1; j++){
                                    int localIndex = Integer.parseInt(pLine[j]);
                                    int localIndexCopy = localIndex;
                                    if (localIndexCopy < 0) localIndex += 1;
                                    else localIndex += -1;

                                    if(j != i){
                                        if(localIndexCopy > 0){
                                            term += (String)programVars.get(localIndex) + " == F";
                                        }
                                        else{
                                            term += (String)programVars.get(Math.abs(localIndex)) + " == T";
                                        }
                                        if(i != pLine.length-1 -1){
                                            if(j != pLine.length-1 - 1) term += " && ";
                                        }
                                        else if(j != pLine.length-3) term += " && ";

                                    }
                                }
                                if(vIndexCopy >= 0){
                                    term += ") -> " + (String)programVars.get(Math.abs(vIndex)) + " = T; goto roll_back;";
                                    inconsistencyCheck += (String)programVars.get(Math.abs(vIndex)) + " == F";
                                }
                                else {
                                    term += ") -> " + (String)programVars.get(Math.abs(vIndex)) + " = F; goto roll_back;";
                                    inconsistencyCheck += (String)programVars.get(Math.abs(vIndex)) + " == T";
                                }
                                if(i != pLine.length-1 - 1) inconsistencyCheck += " && ";
                                else {
                                    inconsistencyCheck += " ));";
                                    contradiction.add(inconsistencyCheck);
                                }
                                clause.add(i,term);
                            }
                            clauseCollection.add(clause);
                        }
                        else{
                            if(pLine.length == 2){
                                String inconsistencyCheck = "(!(";
                                int vIndex = Integer.parseInt(pLine[0]);
                                ArrayList clause = new ArrayList();
                                String term = "(" + (String)programVars.get(Math.abs(vIndex)-1) + " == U) -> ";
                                if(vIndex > 0) {
                                    inconsistencyCheck += (String)programVars.get(Math.abs(vIndex)-1) + "== F";
                                    term += (String)programVars.get(Math.abs(vIndex)-1) + "= T; goto roll_back";
                                }
                                else {
                                    inconsistencyCheck += (String)programVars.get(Math.abs(vIndex)-1) + "== T";
                                    term += (String)programVars.get(Math.abs(vIndex)-1) + "= F; goto roll_back";
                                }
                                inconsistencyCheck += "));";
                                contradiction.add(inconsistencyCheck);
                                clause.add(term);
                                clauseCollection.add(clause);
                            }
                        }
                    }
                }
                /************ print Promela file ************/
                outlnModelCheck("Creating Promela file \"_debug.pml\"...");
                outPG.println(header);
                outPG.println(threeValTypeDef);
                outPG.println();
                outPG.println(startInitBlk);
                outPG.print("\t mtype ");
                for(int i = 0; i < programVars.size(); i++){
                    outPG.print((String)programVars.get(i) + "=U");
                    if(i != programVars.size()-1) outPG.print(", ");
                    else outPG.print(";\n");
                }
                outPG.println();
                outPG.println("\t do");
                outPG.println("\t\t ::true -> ");

                outPG.println("\t\t\t roll_back:");
                for(int i = 0; i < clauseCollection.size(); i++){
                    ArrayList clause = (ArrayList) clauseCollection.get(i);
                    outPG.println("\t\t\t if");
                    for(int j = 0; j < clause.size(); j++){
                        outPG.println("\t\t\t\t ::" + (String)clause.get(j));
                    }
                    outPG.println("\t\t\t\t ::else -> skip;");
                    outPG.println("\t\t\t fi;");
                    outPG.println("\t\t\t assert" + (String)contradiction.get(i));
                    outPG.println();
                }
                outPG.println("\t\t\t if");
                for(int i = 0; i < visibleFeatureVars.size(); i++){
                    String visibleVar = (String)visibleFeatureVars.get(i);
                    if(visibleVar.startsWith("_")){
                        visibleVar = ((String)visibleFeatureVars.get(i)).substring(1);
                    }
                    outPG.println("\t\t\t\t ::" + (String)visibleFeatureVars.get(i) + "==U -> " +
                            (String)visibleFeatureVars.get(i) + " = T; printf(\"--->choose " + visibleVar + "\\n\");");
                }
                outPG.println("\t\t\t\t ::else -> break;");
                outPG.println("\t\t\t fi;");
                outPG.println();
                outPG.println("\t od;");
                outPG.println("\t finish: skip;");
                outPG.println("}");
                outPG.close();
                in.close();
                fInputStream.close();
                fOutputStream.close();
                outlnModelCheck("_debug.pml created successfully!!!");
                return true;
            }
            catch (Exception e){
                outlnModelCheck("cannot open file" + e);
                return false;

            }


}

/*****************************************/
    static void modelDebug( String filename, boolean saveInFile )
     throws dparseException, IOException {
        boolean result;
          variable var = null;

        // Step 0 -- for debugging cnf files
                  // variable.dumpVariablesInOrder();

        // Step 1: create model cnf file string
        //
        cnfModel model = cnfModel.init();
        boolean testSucceeded = true;

        outln( "Beginning test : " + filename );
        outln();

        dparser d = new dparser( filename );
        while ( true ) {
            SATtest t = d.getNextTest();
            if ( t == null )
                break;

            if (t.isComplete()) {
                   // temporarily remove UserSelections
                    ArrayList orig = grammar.UserSelections;
                    grammar.UserSelections = new ArrayList();

               // now perform the complete test
                    // convert strings into variable references
                    grammar.UserSelections.clear();
                    Vector v = t.getSelections();
                    for (int i=0; i<v.size(); i++) {
                       var = (variable) (var.Vtable.get(v.get(i)));
                        grammar.UserSelections.add(var);
                    }
                    grammar.propagate();
                    testSucceeded = reportResult( t.getName(),
                                    cnfClause.complete(false) == t.isSat, testSucceeded);

                    // replace original UserSelections
                    grammar.UserSelections = orig;
                    continue;
                }

            // create the file to be tested, invoke the solver
            // and output the result

            SATSolver s = new SATSolver();
            if ( saveInFile ) {
                createOutputFile( model, t );
                result = s.solve( input2SATSolver );
            }
            else { // use in-memory file
                createOutputBuffer( model, t );
                result = s.solve( new LineNumberReader( new StringReader( cnfFileString ) ) );
            }

            testSucceeded = reportResult( t.getName(),
                                result == t.isSat, testSucceeded );
        }
        outln();
        out( "Summary of " + filename + " test : " );
        if ( testSucceeded )
            outln( " ALL SUCCEEDED" );
        else
            outln( " SOME FAILED" );
    }

/******************** Functions for the ModelChecker ********************/
     static boolean reportResultModelCheck( String testname, boolean resultOfTest, boolean testSucceeded ) {
        if (resultOfTest) {
          outlnModelCheck( "\t\tsucceeded ... " + testname );
             return testSucceeded;
         }
       else {
          outlnModelCheck( "\t\tFAILED    ... " + testname );
          return false;
         }
    }

    static void outlnModelCheck( String x ) {
        if ( ModelCheckerGui.itsme == null )
            System.out.println( x );
        else
            ModelCheckerGui.itsme.println( x );
    }

    static void outlnModelCheck() {
        outlnModelCheck( "" );
    }

    static void outModelCheck( String x ) {
        if ( ModelCheckerGui.itsme == null )
            System.out.println( x );
        else
            ModelCheckerGui.itsme.print( x );
    }

/************** functions for the SAT solver **********************/
     static boolean reportResult( String testname, boolean resultOfTest, boolean testSucceeded ) {
        if (resultOfTest) {
          outln( "succeeded ... " + testname );
             return testSucceeded;
         }
       else {
          outln( "FAILED    ... " + testname );
          return false;
         }
    }

    static void outln( String x ) {
        if ( ModelDebuggerGui.itsme == null )
            System.out.println( x );
        else
            ModelDebuggerGui.itsme.println( x );
    }

    static void outln() {
        outln( "" );
    }

    static void out( String x ) {
        if ( ModelDebuggerGui.itsme == null )
            System.out.println( x );
        else
            ModelDebuggerGui.itsme.print( x );
    }

    static void createOutputFile( cnfModel model, SATtest t )
     throws IOException, dparseException {
        PrintWriter pw = new PrintWriter( new FileWriter( input2SATSolver, false ) );
        createFile(pw, model, t);
        pw.close();
    }

    static String cnfFileString = "";

    static void createOutputBuffer( cnfModel model, SATtest t )
     throws IOException, dparseException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw );
        createFile(pw, model, t);
        pw.close();
        cnfFileString = sw.toString();
        // System.out.println(cnfFileString);
    }

    static void createFile( PrintWriter pw, cnfModel model, SATtest t )
     throws IOException, dparseException {
        cnfout out = new cnfout();
        t.toCnfFormat( out );
        int nclause = model.nclause + out.getCnt();
        pw.println( "p cnf " + model.nvars + " " + nclause + " ");
        variable.dumpVariablesInOrder(pw);
        pw.print( model.model );
        pw.println( out.toString() );
    }

    /********* Functions for explosive check *****************/
    static void checkModel(){
        System.out.println("checking model ...");
        boolean inconsistency = false;
        int saturation = -1;
        int iconVarIndex = -1;
        HashMap varCollection = variable.Vtable;
        Iterator currentVar = varCollection.values().iterator();
        ArrayList userVars = new ArrayList();
        ArrayList selectedSet = new ArrayList();
        while(currentVar.hasNext()){
            variable var = (variable)currentVar.next();
            if(var.userVisible){
                userVars.add(var);
            }
        }
        for(int setNum = 0; setNum < userVars.size(); setNum++){
            //selectedSet = getUserSelectionsList(setNum, userVars);
            //Iterator itSelectedSet = selectedSet.iterator();
            //if(selectedSet.size() == 0 && setNum > 0)
                //break;
            if(saturation == -1 || saturation == 0)
                saturation = 1;
            else
                break;
            combArr = null;
            pivot = -1;
            ArrayList userSelections;
            System.out.println("generating selections..." + setNum + " out of " + userVars.size());
            while((userSelections = getUserSelectionsList(setNum, userVars)) != null){
                saturation = 0;
                //ArrayList userSelections = (ArrayList)itSelectedSet.next();
                if(userSelections.size() != setNum)
                    continue;
                for(int LFVarIndex=0; LFVarIndex<userVars.size();){
                    //reset root and grammar
                    grammar.reset();
                    cnfClause.stack = new Stack();
                    variable root = grammar.getRoot();
                    root.resetRoot();
                    if(!root.setNoDialog(false)){
                        root.justify();
                        inconsistency = true;
                        break;
                    }
                    root.isRoot = true;
                    root.modelSet=true;
                    if(!cnfClause.MC_BCP() || !cnfClause.MC_propagateConstants()){
                        inconsistency = true;
                        break;
                    }
                    // always set/clear any cnf clause with one term
                    /*if(!cnfClause.MC_propagateConstants()){
                     inconsistency = true;
                     break;
                     }*/


                    //set the vars from the selected set one by one and BCP
                    for(int i=0; i<userSelections.size(); i++){
                        variable localVar = (variable)userSelections.get(i);
                        if(localVar.value == variable.U){
                            //System.out.println("\t"+localVar.name);
                            localVar.justify();
                            localVar.setNoDialog(false);
                            cnfClause.MC_BCP();
                        }
                    }
                    //look forward
                    while(true){
                        if(LFVarIndex == userVars.size()) break;
                        variable LFVar= (variable)userVars.get(LFVarIndex);
                        // if the feature already has a value due the BCP of vars
                        // from the selected set skip that feature
                        if(LFVar.value == variable.U){
                            //System.out.println("\t\t"+LFVar.name + LFVarIndex);
                            LFVar.justify();
                            if(!LFVar.setNoDialog(false) || !cnfClause.MC_BCP()){
                                iconVarIndex = LFVarIndex;
                                inconsistency = true;
                                break;
                            }
                            //LFVar.justify();
                            /*if(!cnfClause.MC_BCP()){
                             iconVarIndex = LFVarIndex;
                             inconsistency = true;
                             break;
                             }*/
                            LFVarIndex++;
                            break;
                        }
                        else LFVarIndex++;
                    }
                    if(inconsistency){
                        break;
                    }
                }
                if(inconsistency){
                    System.out.println("Inconsistency detected");
                    System.out.println("choose features in the following order");
                    for(int inconIndex=0; inconIndex<userSelections.size(); inconIndex++){
                        System.out.println("\t--->" + ((variable)userSelections.get(inconIndex)).name);
                        //String proof = ((variable)userSelections.get(inconIndex)).explainValue();
                        //System.out.println(proof);
                        //Area.append(proof + "\n");
                    }
                    if(iconVarIndex != -1){
                        System.out.println("\t--->" + ((variable)userVars.get(iconVarIndex)).name);
                        //String proof = ((variable)userSelections.get(iconVarIndex)).explainValue();
                        //System.out.println(proof);
                        //Area.append(proof + "\n");
                    }
                    //reset all features and grammar
                    grammar.reset();
                    cnfClause.stack = new Stack();
                    variable root = grammar.getRoot();
                    root.resetRoot();
                    break;
                }
            }
            if(inconsistency) break;
        }
        if(!inconsistency){
            System.out.println("No errors detected in model\n");
            grammar.reset();
            cnfClause.stack = new Stack();
            variable root = grammar.getRoot();
            root.resetRoot();
        }
    }

    static ArrayList getUserSelectionsList(int setNum, ArrayList userVars){
        //System.out.println("generating selections..." + setNum + " out of " + userVars.size());
        ArrayList selectionsList = new ArrayList();
        boolean inconsistency = false;
        //int[] c = null;
        //int pivot = -1;
        while (true) {
            combArr = (int[])nextElement(0,userVars.size()-1,setNum,combArr,pivot);
            //if(combArr==null) break;
            if(combArr==null) return null;

            grammar.reset();
            cnfClause.stack = new Stack();
            variable root = grammar.getRoot();
            root.resetRoot();
            if(!root.setNoDialog(false)){
                inconsistency = true;
                break;
            }
            root.isRoot = true;
            root.modelSet=true;
            if(!cnfClause.MC_BCP() || !cnfClause.MC_propagateConstants()){
                inconsistency = true;
                break;
            }
            ArrayList userSelections = new ArrayList();
            /*for(int i = 0; i < c.length; i++){
             System.out.println(c[i] + " ");
             }*/
            for(int i = 0; i < combArr.length; i++){
                int index = combArr[i];
                variable LFVar = (variable) userVars.get(index);
                //System.out.println("-->" + LFVar.name);
                if(LFVar.value != variable.U){
                    //if(fasterSearch.isSelected())
                        pivot = i;
                    //System.out.println("pivot value = "+pivot);
                    break;
                }
                else{
                    LFVar.setNoDialog(false);
                    if(!cnfClause.MC_BCP() || !cnfClause.MC_propagateConstants()){
                        inconsistency = true;
                        break;
                    }
                    userSelections.add(LFVar);
                }
            }
            if(userSelections.size() == setNum){
                pivot = -1;
                selectionsList = userSelections;
                break;
            }
        }
        //System.out.println(" size of the set of user-selections List:" + selectionsList.size() + "\n");
        return selectionsList;
    }




    static public Object nextElement(int lo, int hi, int n, int[] c, int pivot_ref) {

        if (c == null){
            c= new int[n];
            for (int i= 0, j= lo; i < n; c[i++]= j++);
            return c;
        }
        int pivot = -1;
        if (c == null) pivot = 0;
        else{
            for (int i= n; --i >= 0; ){
                if (c[i] <= hi-(n-i)){
                    pivot = i;
                    break;
                }
            }
        }
        if( pivot_ref >=0 && pivot >= 0 && pivot_ref < pivot) {
            pivot = pivot_ref;
        }
        if (pivot < 0) return null;
        for (int j= c[pivot]; pivot < n; c[pivot++]= ++j);
        return c;
    }
}
