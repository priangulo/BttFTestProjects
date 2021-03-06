package guidsl;//created on: Sun Jun 17 16:50:52 CDT 2007

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;

public class ModelCheckerGui extends  SwingDialog {
    public static ModelCheckerGui itsme = null;
    JFrame owner = null;
    int[] combArr = null;
    int pivot = -1;
    HashMap prevCombArrMap = new HashMap();
    boolean debug = false;
    boolean debug_comb_verify = false;
    boolean interactive = false;

    // initialize constants used in the application
    // REMEMBER -- make constants static!

    public void initConstants() {

    }

    // declare and initialize atomic components here

    JButton RunSpin;
    JButton checkModel;
    JButton interpretModel;
    JButton clear;
    JTextArea Area;
    JTextField hashLength;
    JLabel labelHashLength;
    JTextField depthValue;
    JLabel labelDepth;
    JTextArea textComment;
    JLabel labelComment;
    JCheckBox shortestPath;
    JCheckBox fasterSearch;
    JScrollPane JSPComment;
    JScrollPane jsp;
    String comment;


    public void initAtoms() {
        RunSpin = new JButton("Run Spin");
        RunSpin.setToolTipText("Run a thorough check of the model");
        RunSpin.setBorder( BorderFactory.createRaisedBevelBorder() );
        checkModel = new JButton("Exhaustive Check");
        checkModel.setBorder(BorderFactory.createRaisedBevelBorder());
        interpretModel = new JButton("Interpret Model");
        interpretModel.setToolTipText("Check out the sequence of user choices");
        interpretModel.setBorder( BorderFactory.createRaisedBevelBorder() );
        interpretModel.setEnabled(false);
        clear = new JButton("Clear");
        clear.setToolTipText("clear the text area and comments");
        clear.setBorder(BorderFactory.createRaisedBevelBorder());
        Area = new JTextArea( 15, 30 );
        Area.setEditable(false);
        hashLength = new JTextField(2);
        hashLength.setText("23");
        labelHashLength = new JLabel("Hash Length");
        depthValue = new JTextField();
        labelDepth = new JLabel("Search Depth");
        depthValue.setText("10000");
        labelComment = new JLabel("Comments:");
        textComment = new JTextArea(5, 30);
        textComment.setEditable(false);
        shortestPath = new JCheckBox("Shortest Path");
        shortestPath.setToolTipText("Give the min number of user selections for inconsistency");
        shortestPath.setSelected(false);
        fasterSearch = new JCheckBox("Fast Search");
        fasterSearch.setSelected(false);
        JSPComment = new JScrollPane(textComment);
        jsp = new JScrollPane( Area );
        comment = new String("");
    }

    // declare and initialize layout components here

    JPanel pCheckModel;
    JPanel pShortestPath;
    JPanel pFasterSearch;
    JPanel SpinFields;
    JPanel LookForwardFields;
    JPanel HashFields;
    JPanel Empty1;
    JPanel DepthFields;
    JPanel Buttons;
    JPanel ControlSpecs;
    JPanel PText;
    public void initLayout() {
        pCheckModel = new JPanel();
        pShortestPath = new JPanel();
        pFasterSearch = new JPanel();

        PText = new JPanel();
        ControlSpecs = new JPanel();
        SpinFields = new JPanel();
        LookForwardFields = new JPanel();
        Buttons = new JPanel();
        HashFields = new JPanel();
        DepthFields = new JPanel();
        Empty1 = new JPanel();

        SpinFields.setBorder(BorderFactory.createEtchedBorder());
        LookForwardFields.setBorder(BorderFactory.createEtchedBorder());
        pCheckModel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pShortestPath.setLayout(new FlowLayout(FlowLayout.LEFT));
        pFasterSearch.setLayout(new FlowLayout(FlowLayout.LEFT));

        PText.setLayout(new BoxLayout(PText, BoxLayout.Y_AXIS));
        ControlSpecs.setLayout(new BoxLayout(ControlSpecs, BoxLayout.Y_AXIS));
        SpinFields.setLayout(new GridLayout(0,1));
        LookForwardFields.setLayout(new GridLayout(0,1));
        Buttons.setLayout( new GridLayout(0,1) );
        HashFields.setLayout(new GridLayout(0,1));
        DepthFields.setLayout(new GridLayout(0,1));
        HashFields.add(labelHashLength);
        HashFields.add(hashLength);
        DepthFields.add(labelDepth);
        DepthFields.add(depthValue);
        Buttons.add(RunSpin);
        Buttons.add(interpretModel);
        Buttons.add(clear);
        pShortestPath.add(shortestPath);
        pCheckModel.add(checkModel);

        SpinFields.add(HashFields);
        SpinFields.add(DepthFields);
        SpinFields.add(Empty1);
        SpinFields.add(Buttons);
        SpinFields.add(pShortestPath);
        LookForwardFields.add(pCheckModel);

        ControlSpecs.add(LookForwardFields);
        ControlSpecs.add(SpinFields);
        PText.add(jsp);
        PText.add(labelComment);
        PText.add(JSPComment);
    }

    // initialize ContentPane here

    public void initContentPane() {
        ContentPane = new JPanel();
        ContentPane.setLayout( new FlowLayout(FlowLayout.LEFT) );
        ContentPane.setBorder(BorderFactory.createEtchedBorder());
        ContentPane.add(ControlSpecs);
        ContentPane.add(PText);
    }

    // initialize listeners here

    public void initListeners() {
        RunSpin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Date dt = new Date();
                long start = dt.getTime();

                comment = "";
                textComment.setText("");
                // create the _debug.cnf file
                try{
                    Area.setText("");
                    Area.append("Creating _debug.cnf...\n");
                    solverTest.createCNF();
                }
                catch (Exception ee) {
                    Area.append("Exception in creating the .cnf file" +
                            ee.getMessage() + "\n");
                }

                //generate the Promela code file
                try{
                    Area.append("\nConverting _debug.cnf to Promela code file...\n");
                    boolean bPromela = solverTest.promelaGenerate("_debug.cnf");
                    if(!bPromela) Area.append("Generation of Promela code file... UNSUCCESSFUL\n");
                }
                catch(Exception ee){
                    Area.append("cannot convert cnf to promela" +
                            ee.getMessage() + "\n");
                }

                //Create pan.c using spin
                try{
                    Area.append("\nCreating pan.c using Spin\n");
                    String[] spinCommand = {"spin", "-a", "_debug.pml"};
                    Process spin = Runtime.getRuntime().exec(spinCommand);
                    BufferedReader spinReader = new BufferedReader(
                            new InputStreamReader(spin.getInputStream())
                    );
                    String spinOutput;
                    while((spinOutput = spinReader.readLine()) != null) {
                        Area.append(spinOutput + "\n");
                    }
                    spinReader.close();
                    spin.waitFor();

                }
                catch (Exception ee){
                    Area.append("cannot execute the spin command" +
                            ee.getMessage() + "\n");
                }

                //compile pan.c and create pan.exe
                try{
                    Area.append("\nCompiling pan.c...\n");
                    String[] spinCommand = {"gcc", "-DBITSTATE", "-DSAFETY", "-o", "pan", "pan.c"};
                    //String[] spinCommand = {"gcc","-DSAFETY", "-o", "pan", "pan.c"};
                    Process spin = Runtime.getRuntime().exec(spinCommand);
                    BufferedReader spinReader = new BufferedReader(
                            new InputStreamReader(spin.getInputStream())
                    );
                    String spinOutput;
                    while((spinOutput = spinReader.readLine()) != null) {
                        Area.append(spinOutput + "\n");
                    }
                    spinReader.close();
                    spin.waitFor();

                }
                catch (Exception ee){
                    Area.append("cannot compile pan.c" +
                            ee.getMessage() + "\n");
                }

                //run pan
                try{
                    String strHashLength = hashLength.getText();
                    String strDepth = depthValue.getText();
                    Area.append("\nRun pan.exe to explore states\n");
                    String[] spinCommand;
                    if(shortestPath.isSelected()){
                        String[] temp = {"pan", "-w"+strHashLength, "-m"+strDepth, "-i"};
                        spinCommand = (String [])temp.clone();
                    }
                    else{
                        String[] temp = {"pan", "-w"+strHashLength, "-m"+strDepth};
                        spinCommand = (String [])temp.clone();
                    }
                    Process spin = Runtime.getRuntime().exec(spinCommand);
                    BufferedReader spinReader = new BufferedReader(
                            new InputStreamReader(spin.getInputStream())
                    );
                    String spinOutput;
                    int error = 0;
                    while((spinOutput = spinReader.readLine()) != null) {
                        if((error = parseOutput(spinOutput)) == -1) break;
                        //if(error == -1) break;
                    }
                    if(error == 0) textComment.append("No errors found in model\n");
                    textComment.append(comment);

                    spinReader.close();
                    spin.waitFor();

                }
                catch (Exception ee){
                    Area.append("cannot run pan.exe command" +
                            ee + "\n");
                }
                dt = new Date();
                long end = dt.getTime();
                System.out.println("Total time taken: " + (float)(end-start)/1000.0 + " seconds");
                Area.append("Total time taken: " + (float)(end-start)/1000.0 + " seconds\n");
            }
        });

        interpretModel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // run the _debug.pml.trail file using spin
                try{
                    Area.setText("");
                    textComment.setText("");
                    Area.append("\n running trail file...\n");
                    Area.append("choose the features in the following order for finding \n out the inconsistency\n");
                    String[] spinCommand = {"spin", "-t", "_debug.pml"};
                    Process spin = Runtime.getRuntime().exec(spinCommand);
                    BufferedReader spinReader = new BufferedReader(
                            new InputStreamReader(spin.getInputStream())
                    );
                    String spinOutput;
                    while((spinOutput = spinReader.readLine()) != null) {
                        parseTrailOutput(spinOutput);
                        //Area.append(spinOutput + "\n");
                    }
                    spinReader.close();
                    spin.waitFor();

                }
                catch(Exception ee){
                    Area.append("error in running trail file\n" + ee);
                }
            }
        });

        clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // clear textarea and comments
                Area.setText("");
                textComment.setText("");
            }
        });

        checkModel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Date dt = new Date();
                long start = dt.getTime();
                checkModel();
                dt = new Date();
                long end = dt.getTime();
                Area.append(cnfClause.reasonOut);
                System.out.println("Total time taken: " + (float)(end-start)/1000.0 + " seconds");
                Area.append("Total time taken: " + (float)(end-start)/1000.0 + " seconds\n");
            }
        });
    }

       void checkModel(){
           //declarations
           boolean inconsistency;
           int iconVarIndex;//to store the index of the variable that caused the inconsistency
           HashMap varCollection;
           Iterator currentVar;//iterator for varCollection
           ArrayList userVars;//user visible features
           ArrayList selectedSet;//TODO

           //start comments
           System.out.println("checking model ...");
           Area.append("checking model ...\n");

           //initialization
           inconsistency = false;
           iconVarIndex = -1;
           userVars = new ArrayList();
           selectedSet = new ArrayList();
           varCollection = variable.Vtable;

           //extract the user-visible vars from the varCollection (hashmap)
           currentVar = varCollection.values().iterator();
           while(currentVar.hasNext()){
               variable var = (variable)currentVar.next();
               if(var.userVisible){
                   userVars.add(var);
                   if(debug) System.out.println(var.name);//TODO: declare boolean debug
               }
           }

           //start checking
           int setNum = 0;// indicates the kth step in ICA
           int saturation = -1;//is set when we have all possible combinations
           int count = 0;// a statistics variable
           for(setNum = 0; setNum < userVars.size(); setNum++){
               //declarations and initialization
               ArrayList userSelections;//stores the current sequence being checked
               combArr = null;//before you start a new step clear the combinations array;
               pivot = -1;//and reset the pivot - used generating combinations
               //count = 0;//reset stats

               //loop entry checks
               if(saturation == -1 || saturation == 0)
                   saturation = 1;
               else
                   break;

               System.out.println("generating selections..." + setNum + " out of " + userVars.size() + "count: " + count);
               while((userSelections = getUserSelectionsList(setNum, userVars)) != null){
                   //loop initializations
                   saturation = 0;

                   //loop entry checks
                   if(userSelections.size() != setNum)
                       continue;

                   //Look Forward(LF) stage
                   count++;
                   //if(count % 100 == 0) System.out.println("count = " + count);
                   for(int LFVarIndex=0; LFVarIndex<userVars.size();){
                       //clear the history of selected variables before starting to propagate
                       variable.selectedVars.clear();

                       //look forward
                       while(true){
                           if(LFVarIndex == userVars.size()) break;//obvious
                           variable LFVar= (variable)userVars.get(LFVarIndex);

                           // if the feature already has a value due the BCP of vars
                           // from the selected set skip that feature
                           if(LFVar.value == variable.U){
                               if(debug_comb_verify) System.out.println("\t\t"+LFVar.name + LFVarIndex);
                               LFVar.justify();
                               if(!LFVar.setNoDialog(false) || !cnfClause.MC_BCP()){
                                   iconVarIndex = LFVarIndex;
                                   inconsistency = true;
                                   break;
                               }
                               LFVarIndex++;
                               break;
                           }
                           else LFVarIndex++;
                       }
                       if(inconsistency){
                           break;
                       }

                       //every call to setNoDialog()will store the variable in selectedVars
                       //clean up the selected vars for this iteration of the look forward stage
                       Iterator cleanIter = variable.selectedVars.iterator();
                       while(cleanIter.hasNext()){
                           String name = (String)cleanIter.next();
                           variable cleanVar = (variable)varCollection.get(name);
                           cleanVar.reset();
                       }
                   }

                   if(inconsistency){
                       System.out.println("Inconsistency detected");
                       System.out.println("choose features in the following order");
                       Area.append("Inconsistency detected\n");
                       Area.append("choose features in the following order\n");
                       for(int inconIndex=0; inconIndex<userSelections.size(); inconIndex++){
                           System.out.println("\t--->" + ((variable)userSelections.get(inconIndex)).name);
                           Area.append("\t--->" + ((variable)userSelections.get(inconIndex)).name + "\n");
                           if(debug) {
                               String proof = ((variable)userSelections.get(inconIndex)).explainValue();
                               System.out.println(proof);
                           }
                       }
                       if(iconVarIndex != -1){
                           System.out.println("\t--->" + ((variable)userVars.get(iconVarIndex)).name);
                           Area.append("\t--->" + ((variable)userVars.get(iconVarIndex)).name + "\n");
                           if(debug) {
                               String proof = ((variable)userSelections.get(iconVarIndex)).explainValue();
                               System.out.println(proof);
                           }
                       }

                       //reset all features and grammar
                       grammar.reset();
                       cnfClause.stack = new Stack();
                       variable root = grammar.getRoot();
                       root.resetRoot();
                       variable.selectedVars.clear();
                       break;
                   }
               }
               if(inconsistency) break;
           }
           if(!inconsistency){
               System.out.println("No errors detected in model\n");
               Area.append("Model Checking Complete");
               Area.append("No errors detected in model\n");
               grammar.reset();
               cnfClause.stack = new Stack();
               variable root = grammar.getRoot();
               root.resetRoot();
               variable.selectedVars.clear();
           }
       }

       //this function the names and the values assumed by the variables of the passed arrayList
       void printUserVars(ArrayList userVars){
           for (int i = 0; i < userVars.size(); i++){
               variable local_var = (variable)userVars.get(i);
               System.out.println(local_var.name + " " + local_var.value);
           }
       }

       //get a valid sequence
       ArrayList getUserSelectionsList(int setNum, ArrayList userVars){
           try{
               if(interactive) System.in.read();
           }
           catch(Exception e){
           // do nothing
           }
           //declarations and initializations
           ArrayList selectionsList = new ArrayList();
           boolean inconsistency = false;
           int[] prevCombArr = null;
           HashMap varCollection = variable.Vtable;

           //store the previous combination
           if(combArr != null)
               prevCombArr = (int[])combArr.clone();
           while (true) {
               combArr = (int[])nextElement(0,userVars.size()-1,setNum,combArr,pivot);

               if(debug) printUserVars(userVars);
               if(debug){
                   if(prevCombArr != null){
                       System.out.print("prevcombarr: ");
                       for(int k = 0; k < prevCombArr.length; k++){
                           System.out.print(((variable)userVars.get(prevCombArr[k])).name+ " ");
                       }
                       System.out.println();
                   }
                   else System.out.println("prevcombarr: null");
                   if(combArr != null){
                       System.out.print("currcombarr: ");
                       for(int k = 0; k < combArr.length; k++){
                           System.out.print(((variable)userVars.get(combArr[k])).name + " ");
                       }
                       System.out.println();
                   }
                   else System.out.println("currcombarr: null");
               }

               //if there are no more combinations, reset all the selections in the previous combinations
               if(combArr==null){
                   pivot = -1;
                   for(int k = 0; k < prevCombArr.length; k++){
                       int prev_index = prevCombArr[k];
                       String name = ((variable)userVars.get(prev_index)).name;
                       ArrayList cleanList = (ArrayList)(prevCombArrMap.get(name));
                       if(cleanList == null) break;
                       prevCombArrMap.remove(name);
                       Iterator cleanListIter = cleanList.iterator();
                       while(cleanListIter.hasNext()){
                           String cleanName = (String)cleanListIter.next();
                           ((variable)varCollection.get(cleanName)).reset();
                           if(debug) System.out.println("cleaning up: " + cleanName);
                       }
                   }
                   prevCombArr = null;
                   return null;
               }

               //check the combArr with prevCombArr for matching values
               int i = 0;//this holds the index from where the two sequences differ
               if(prevCombArr != null){
                   for(i = 0; i < combArr.length; i++){
                       if(combArr[i] != prevCombArr[i]) break;
                   }
                   //reset all the vars and their propagations that differ
                   for(int j = i; j < combArr.length; j++){
                       int prev_index = prevCombArr[j];
                       String name = ((variable)userVars.get(prev_index)).name;
                       ArrayList cleanList = (ArrayList)(prevCombArrMap.get(name));
                       if(cleanList == null) break;
                       prevCombArrMap.remove(name);
                       Iterator cleanListIter = cleanList.iterator();
                       while(cleanListIter.hasNext()){
                           String cleanName = (String)cleanListIter.next();
                           ((variable)varCollection.get(cleanName)).reset();
                           if(debug) System.out.println("cleaning up: " + cleanName);
                       }
                   }
               }
               prevCombArr = (int[])combArr.clone();
               if(debug)printUserVars(userVars);
               //optimization code ends here

               //add the selections that remained in the previous sequence
               ArrayList userSelections = new ArrayList();//holds the valid user sequence
               for(int j =0; j < i; j ++){
                   int index = combArr[j];
                   variable LFVar = (variable) userVars.get(index);
                   userSelections.add(LFVar);
               }

               //propagate the selections of the new sequence
               for(int j = i; j < combArr.length; j++){
                   variable.selectedVars.clear();
                   int index = combArr[j];
                   variable LFVar = (variable) userVars.get(index);
                   if(debug) System.out.println("-->" + LFVar.name);

                   //if the variable is already known set the pivot and get another sequence
                   if(LFVar.value != variable.U){
                       pivot = j;
                       if(debug) printUserVars(userVars);
                       if(debug) System.out.println("pivot value = "+pivot);
                       break;
                   }
                   else{
                       /* 1. set the variable and propagate constraints
                        * 2. record the propagation in the hash map(prevcombarrmap)
                        * 3. add to the set of valid user selections
                        */
                       LFVar.setNoDialog(false);
                       LFVar.justify();
                       if(!cnfClause.MC_BCP() || !cnfClause.MC_propagateConstants()){
                           inconsistency = true;
                           break;
                       }
                       userSelections.add(LFVar);
                       ArrayList selectedVars = (ArrayList)variable.selectedVars.clone();
                       prevCombArrMap.put(LFVar.name, selectedVars);
                       if(debug) printUserVars(userVars);
                   }
               }
               if(userSelections.size() == setNum){
                   selectionsList = userSelections;
                   if(debug_comb_verify){
                        Iterator iterSelectionList = userSelections.iterator();
                        while(iterSelectionList.hasNext()){
                            variable local_var = (variable) iterSelectionList.next();
                            System.out.println("\t" + local_var.name);
                        }
                   }
                   break;
               }
           }
           return selectionsList;
       }


    public Object nextElement(int lo, int hi, int n, int[] c, int pivot_ref) {

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

    int parseTrailOutput(String spinTrail){
        String[] token;
        token = spinTrail.split("\\s+");
        //System.out.println(token[0]);
        if(token[1].startsWith("--->"))
        {
            Area.append(spinTrail + "\n");
            return 1;
        }
        return 0;
    }

    int parseOutput(String spinOutput){
        String[] token;
        int colon;
        colon = spinOutput.indexOf(":");
        if(colon == -1) return 0;
        token = spinOutput.split("\\s+");
        //System.out.println(token[0]);
        switch (token[0].charAt(0)){
        case 'p':
            if(token.length >= 3){
                if((token[1].compareTo("assertion") == 0) && (token[2].compareTo("violated") == 0)){
                    String assertion;
                    Area.setText("");
                    Area.append("<<<<<<< MODEL INCONSISTENCY DETECTED >>>>>>: \n");
                    comment += "Model Checking COMPLETE\n" +
                    "ERROR in model\n" +
                    "Please run Debug Model to getting the input trace\n";
                    assertion = token[3].replaceAll("2", "F");
                    assertion = assertion.replaceAll("3", "T");
                    Area.append(assertion + "\n");
                    interpretModel.setEnabled(true);
                    return -1;
                }
            }
            if(token.length >= 4){
                if((token[1]).compareTo("out") == 0){
                    Area.setText("");
                    Area.append("System cannot support the specified size of the hash table\n");
                    comment += "Model Checking INCOMPLETE\n" +
                    "Please reduce the Hash Length\n";
                    return -1;
                }
            }
            break;
        case 'W':
            if(token[0].compareTo("Warning") == 0) Area.append(spinOutput + "\n");
            break;
        case 'h':
            if(token.length >= 2){
                if(token[1].compareTo("factor:") == 0){
                    //System.out.println("factor: "+token[2]);
                    String[] intToken = token[2].split("\\.");
                    //System.out.println("token: " + intToken[0] + "\n");
                    int hashFactor = Integer.parseInt(intToken[0]);
                    if(hashFactor < 100)
                    {
                        comment += "Only " + hashFactor + "% of the total state space is explore\n" +
                        "Increase of Hash Length recommended\n";
                    }
                    Area.append(spinOutput + "\n");
                }
            }
            break;
        case 'e':
            if(token[0].compareTo("error:") == 0)
            {
                Area.setText("");
                Area.append(spinOutput + "\n");
                comment += "Model Checking INCOMPLETE\n" +
                "Please increase the Search Depth\n";
                return -1;
            }
            break;
        }
        return 0;
    }

//  place in this method any action for exiting application

    public void applicationExit() {

    }

    void print( String x ) { Area.append(x); }

    void println( String x ) { Area.append(x + "\n");  }

    public ModelCheckerGui(JFrame owner, String AppTitle, boolean modal) {
        super(owner, AppTitle, modal);
        setLocationRelativeTo(owner);
        itsme = (ModelCheckerGui) this;
        this.owner = owner;
    }

}
