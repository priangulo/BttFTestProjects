package guidsl;

import java.util.*;
import javax.swing.JComponent;
import Jakarta.util.*;

public class grammar extends gObj {

    public static grammar current;
    public static production rootProduction;
    public static HashMap productions;

    grammar(String name) {
        super(name);
        productions = new HashMap();
        rootProduction = null;
        production.counter = 0;
        current = (grammar) this;
    }

    public void visit(GVisitor v) {
        v.action(this);
    }

    public void traverse(GVisitor v) {
        rootProduction.visit(v);
    }

    static void makeClauses() {
        cnfClause c = new cnfClause();
        cterm t = new cterm(false);
        t.setVar(rootProduction.var);
        c.add(t);
        c.setFormula(new bterm(rootProduction.var.name));
        if (Main.outputModelPredicate) {
            System.out.println();
            System.out.println("// root");
            System.out.println("(" + rootProduction.var.name + ")");
        }
        cnfClause.clist.add(c);
    }

    // this is the set of selections that are to be propagated by an LTMS
    static ArrayList UserSelections = new ArrayList();

    private static boolean debugIsOn = false;
    private static boolean updateNow = true;
    private static boolean beginning = true;

    static variable root = null;

    static variable getRoot() {
        if (root != null) {
            return root;
        }
        String rootName = (grammar.rootProduction).name;
        root = (variable) (variable.Vtable.get(rootName));
        return root;
    }

    static void initDebugTable() // initialize debug table
    {
        if (beginning) {
            reset();
        }

        DebugTable.createAndShowGUI();
        debugIsOn = true;
    }

    static void initFormulas() // initialize propagation formulas table
    {
        Formulas.createAndShowGUI();
    }

    static void propagate() // clear variables, and propagate UserSelections
    {
        /* DEBUG
System.out.println( " ----------------propagate() called --------------- " );
         */
        beginning = false;

        updateNow = false;
        reset();
        updateNow = true;

        cnfClause.stack = new Stack();

        // always set the root
        variable root = getRoot();
        root.resetRoot();
        root.set(false);
        root.isRoot = true;
        root.modelSet = true;
        cnfClause.BCP();

        // always set/clear any cnf clause with one term
        cnfClause.propagateConstants();

        // now go and set the clauses selected by the user
        Iterator i = UserSelections.iterator();
        while (i.hasNext()) {
            variable v = (variable) i.next();
            v.set(false);   // set variable to be true
            v.justify();    // set by user
            cnfClause.BCP();
        }
        /* DEBUG
System.out.println("----- Propagate done ----");
         */

        if (debugIsOn) { // update debug table components
            for (int row = 0; row < DebugTable.sortedVtable.length; row++) {
                variable v = (variable) (variable.Vtable.get(DebugTable.sortedVtable[row]));
                if (v.value == variable.T) {
                    DebugTable.data[row][1] = "True";
                } else {
                    if (v.value == variable.F) {
                        DebugTable.data[row][1] = "False";
                    } else // v.value == variable.U
                    {
                        DebugTable.data[row][1] = "Unknown";
                    }
                }

                if (v.userSet) {
                    DebugTable.data[row][2] = "True";
                } else {
                    DebugTable.data[row][2] = "False";
                }
            }
            DebugTable.update();
        }
    }

    public static void reset() // reset variables
    {
        Iterator vars = (variable.Vtable.values()).iterator();
        while (vars.hasNext()) {
            variable v = (variable) (vars.next());
            v.reset();
            /* DEBUG
            v.value = variable.U;
            v.userSet = false;
            v.explanation = "";
             */
        }
        variable root = getRoot();
        root.resetRoot();
        /* DEBUG
        root.value = variable.T;
        root.userSet = true;
        root.explanation = "Root";
         */

 /* DEBUG

        if( debugIsOn && updateNow )
        { // update debug table components
            for( int row = 0; row < DebugTable.sortedVtable.length; row++ )
            {
                variable v = ( variable ) ( variable.Vtable.get( DebugTable.sortedVtable[row] ) );
                if( rootName.equals( v.name ) )
                {
                    DebugTable.data[row][1] = "True";
                    DebugTable.data[row][2] = "False";
                }
                else
                {
                    DebugTable.data[row][1] = "Unknown";
                    DebugTable.data[row][2] = "True";
                }
            }
            DebugTable.update();
        }
         */
    }

    static void dumpUserSelections() {
        Iterator i = UserSelections.iterator();
        System.err.println("List of selections that triggered inconsistency:");
        while (i.hasNext()) {
            variable v = (variable) i.next();
            System.err.print(v.name + " ");
        }
        System.err.println("");
    }

    public JComponent draw(int several) {

        // Step 1: do so consistency checking
        if (rootProduction.type != production.norm) {
            Util.fatalError("root production should be choose1");
        }

        // Step 2: return graphic of root production
        return setWidget(rootProduction.draw(0));
    }

    // generates root = true
    public static void toCnfFormat(cnfout out) {
        out.cnfBeginFormula(rootProduction.var.name);
        out.append(rootProduction.var.number + "");
        out.endFormula();
    }

    public static void propagateCnfClause() {
        cnfClause.BCP();
    }
}
