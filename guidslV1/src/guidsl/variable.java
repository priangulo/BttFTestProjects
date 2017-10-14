package guidsl;
import java.util.*;
import Jakarta.util.*;
import javax.swing.*;
import java.io.*;

public class variable implements Comparator {
    public static variable current; // most recently parsed variable
    public static HashMap Vtable; // current set of variables
    public static final int T=1; // true
    public static final int F=0; // false
    public static final int U=-1; // unknown

    public static int vtsize = 0; // size of Vtable

    public static final int Unkn=0; // unknown
    public static final int Sprd=1; // starting production
    public static final int Prod=2; // production
    public static final int Patt=3; // pattern
    public static final int Prim=4; // primitive

    public String getType() {
       if (type==Unkn) return "Unkn";
       if (type==Sprd) return "Sprd";
       if (type==Prod) return "Prod";
       if (type==Patt) return "Patt";
       if (type==Prim) return "Prim";
       Util.fatalError("unknown type for " + name + " is " + type);
       return null;
    } 

    public int     value;
    public String  name;
    public int     type;
    public gObj    gobj;
    public boolean redefine;   // can this be redefined?

    public static final variable define$$dsl$guidsl$gspec( String name, int type, gObj g, boolean redefinable ) {
        if ( vtsize==0 )
            Vtable = new HashMap();
        variable v = ( variable ) Vtable.get( name );
        if (v != null) {
            if (v.redefine) {
               v.redefine = false;
               return v;
            }
            Util.error( "multiple definitions of "+ name );
            return v;
        }
        v = new variable();
        current = v;
        v.name = name;
        v.value = U;
        v.type = type;
        v.gobj = g;
        v.redefine = redefinable;
        Vtable.put( name, v );
        vtsize++;
        return v;
    }

    public void setValue( int v ) {
        value = v;
    }


    public final void print$$dsl$guidsl$gspec() {
        System.out.print( " "+value+" = "+name+ " type=" + getType() + " " );
        if ( gobj == null )
            System.out.print( " gobj not set " );
        else
            if ( !name.equals( gobj.name ) )
                Util.error( "variable name != definition name" );
    }

    public void visit( GVisitor v ) {
        v.action( this );
    }

    public static void dumpVtable() {
        variable v;
        int cnt = 0;
        System.out.println( "-------Begin Vtable Dump----------" );
        Iterator i = Vtable.values().iterator();
        while ( i.hasNext() ) {
            v = ( variable ) i.next();
            v.print();
				System.out.println();
            cnt++;
        }
        System.out.println( cnt + " variables in all." );
        System.out.println( "-------End Vtable Dump----------" );
    }

    public void setgobj( gObj g ) {
        gobj = g;
    }
    public static ArrayList selectedVars = new ArrayList();
    boolean userSet = false; // true if set by user, false otherwise
    boolean modelSet = false;  // true if value of variable set by model
                               // e.g. root = true in all models

    public final void reset$$dsl$guidsl$ltms() {
      userSet = false;
      value = variable.U;
      // to be extended by later layers
    }

    public final void print$$dsl$guidsl$ltms() {
        print$$dsl$guidsl$gspec();
        System.out.print( "userSet is " + userSet );
        System.out.print( "modelSet is " + modelSet );
    }

    public final void justify$$dsl$guidsl$ltms( cnfClause reason ) { /* filled in by later layers */ }

    public final void justify$$dsl$guidsl$ltms( ) {
       userSet = true;
       // extended by later layers
    }

    public final void set$$dsl$guidsl$ltms(boolean negated) {
        // set value of variable to be true

        if (negated)
           value = variable.F;
        else
           value = variable.T;

        // for each clause in clist that contains not this-term

        Iterator i = cnfClause.clist.iterator();
        while ( i.hasNext() ) {
            cnfClause c = ( cnfClause ) i.next();
            if ( c.hasNegTerm( !negated, ( variable )this ) ) {
                if ( c.isUnitOpen() != null )
                    cnfClause.stack.push( c );
                else
                    if ( c.isViolated() ) {
                        grammar.dumpUserSelections();
                        JOptionPane.showMessageDialog( null,
                        "model inconsistency detected -- see stderr for more information",
                        "Error!", JOptionPane.ERROR_MESSAGE );
                        //System.exit(1);
                    }

            }
        }
    }
    public boolean setNoDialog(boolean negated) {
        // set value of variable to be true
        if (negated)
           value = variable.F;
        else
           value = variable.T;
        //store the name of the variable in the selectedVars list (used in model checker)
        selectedVars.add(name);
        // for each clause in clist that contains not this-term

        Iterator i = cnfClause.clist.iterator();
        while ( i.hasNext() ) {
            cnfClause c = ( cnfClause ) i.next();
            if ( c.hasNegTerm( !negated, ( variable )this ) ) {
                if ( c.isUnitOpen() != null )
                    cnfClause.stack.push( c );
                else
                    if ( c.isViolated() ) {
                        cnfClause.ctStr += ((node)c.formula).toString().replace("_","") + "\n";
                        return false;
                        /*grammar.dumpUserSelections();
                        JOptionPane.showMessageDialog( null,
                        "model inconsistency detected -- see stderr for more information",
                        "Error!", JOptionPane.ERROR_MESSAGE );
                        System.exit(1);*/
                    }

            }
        }
        return true;
    }

   // for comparing & sorting variables

   public int compare(Object o1, Object o2) {
      variable v1 = (variable) o1;
      variable v2 = (variable) o2;

      if (v1.whenSet < v2.whenSet) return -1;
      if (v1.whenSet == v2.whenSet) return 0;
      return 1;
   }

   static int timer;
   int whenSet;           // variable assignment order
   node reason;           // formula that lead to the deduction
   ArrayList antecedents; // variables used in computing variable's value
   boolean   isRoot;      // is root variable?

   public void reset() {
      whenSet = 0;
      //System.out.println(name + "reason to null" + reason);
      reason = null;
      antecedents = null;
      isRoot = false;
      reset$$dsl$guidsl$ltms();
   }

   void resetRoot() {
      timer = 1;
      whenSet = 0;
      reason = null;
      antecedents = null;
      isRoot = true;
      reset$$dsl$guidsl$ltms();
   }

   public void set( boolean negated ) {
      set$$dsl$guidsl$ltms(negated);
      whenSet = timer++;
   }

   // forms union of all antecedents

   void collectAntecedents( ArrayList ts ) {
      if (antecedents == null) return;
      Iterator i = antecedents.iterator();
      while (i.hasNext()) {
         variable v = (variable) i.next();
         if (!ts.contains(v)) {
           ts.add(v);
           v.collectAntecedents(ts);
         }
      }
   }

   String shortName() {
      if (name.startsWith("_")) return name.substring(1);
      return name;
   }

   String explainVariable() {
      String x;

      // this is a hack, due to the strange naming conventions.
      // we don't want to generate explanations of the form
      // MSTPrim because (_MSTPrim iff MSTPrim)
      // so we weed out any "reason" that generates (_x iff x)
      //System.out.println(reason + "||" + reason.left + "||" + reason.right);
      if (reason instanceof iff)
         if (reason.left.toString().equals("_"+reason.right.toString()))
            return "";
      //System.out.println("after iff: " + value + "||" + name);
      if (value == variable.U) return "";
      if (value == variable.F) x = "not " + shortName();
      else x = shortName();
      //System.out.println("before gen expl: " + isRoot + "||" + userSet + "||" + modelSet);
      // now generate explanation

      if (isRoot) return x + " because it is root of grammar\n";
      if (userSet) return x + " because set by user\n";
      if (modelSet) return x + " because value set by model\n";
      x = x + " because " + reason.toString().replace("_","") + "\n";
      //System.out.println(x);
      return x;
   }

   String explainValue() {   // returns explanation of why variable has its value
      ArrayList ts = new ArrayList();

      // collect all variables together that contribute to this variable's
      // value

      ts.add((variable) this);
      collectAntecedents( ts );

      // now, iterate over this set in order from ground up

      String result = "";
      Iterator i = ts.iterator();
      /*while(i.hasNext()){
         variable v = (variable) i.next();
         System.out.println("ts: " + v.name + v.reason);
      }
      i = ts.iterator();*/
      while (i.hasNext()) {
         variable v = (variable) i.next();
         //System.out.println("explainValue: " + v.name);
         result = v.explainVariable() + result;
         //System.out.println("returned explainValue: " + v.name);
      }
      return result;
   }


   // justify remembers why a variable's value was set the way it was

   public void justify( cnfClause r ) {
      reason = r.formula;
      antecedents = new ArrayList();

      Iterator i = r.terms.iterator();
      while (i.hasNext()) {
         cterm c = (cterm) i.next();
         if (!c.var.equals((variable) this)) {
             antecedents.add(c.var);
         }
      }

      justify$$dsl$guidsl$ltms(r);
   }

   public void justify() {
      reason = null;
      antecedents = null;
      isRoot = false;
      whenSet = timer++;   // root is always the first
      justify$$dsl$guidsl$ltms();
   }
    JComponent widget = null;
    boolean hidden = false; // is this variable hidden from view as declared
                            // in the model file
    boolean userVisible = false;  // is this feature displayed for selection
                                  // in the GUI?  (assume not)
    String disp = "";
    String help = null;
    String helpfile = null; //turned this into a string, used to be of type File
    boolean tab; // does this production start a new tab?

    public JComponent setWidget( JComponent w ) {
        widget = w;
        return w;
    }

    public static final variable define$$dsl$guidsl$guigs( String name, int type, gObj g, boolean redef ) {
        variable v = define$$dsl$guidsl$gspec( name,type, g, redef );
        if ( v!=null ) {
            // trim off leading "_" if present for display
            if ( name.startsWith( "_" ) )
                v.disp = name.substring( 1 );
            else
                v.disp = name;
        }
        return v;
    }

    public final void print$$dsl$guidsl$guigs() {
        print$$dsl$guidsl$ltms();
        System.out.print( "    hidden=" + hidden + " display: " + disp );
        if ( widget != null )
            System.out.print( " widget: " +widget.getClass().getName() );
    }

    public static void clearUserVisible() {
        Iterator i = Vtable.values().iterator();
        while ( i.hasNext() ) {
            variable v = ( variable ) i.next();
            v.userVisible = false;
        }
    }
   boolean eqn = false;   		// assume no equation to output for this variable
	                           // makes sense only for productions
   String  out;               // what to output in .equations file
                              // only for primitives
   boolean reverse;           // output ordering of .equations file


   public void print() {
     print$$dsl$guidsl$guigs();
     System.out.print( " eqn ="  + eqn );
   }

   public static final variable define$$dsl$guidsl$output( String name, int type, gObj g, boolean b ) {
      variable v = define$$dsl$guidsl$guigs(name,type,g, b);
      if (v!=null)
         v.out = name;
      return v;
	}
    int number = -1;

    public static variable define( String name, int type, gObj g, boolean redefinable ) {
        variable result = define$$dsl$guidsl$output( name, type, g, redefinable );
        if ( result.number == -1 ) {
            result.number = variable.vtsize;
                       // for debugging cnf files
        // System.out.println(result.number + " " +result.name);
        }
        return result;
    }

    static variable find( String name ) {
        return ( variable ) Vtable.get( name );
    }

    static int findNumber( String name ) throws dparseException {
        variable v = find( name );
        if ( v != null )
            return v.number;
        throw new dparseException( "unrecognizable variable: " + name );
    }

    // for debugging cnf files
    static void dumpVariablesInOrder( PrintWriter pw ) {
        try {
            for ( int i=0; i<Vtable.size(); i++ ) {
                int v = i+1;
                String varname = findVar(v);
                variable var = find(varname);
                if (var.userVisible)
                   pw.println( "c u " + v + " " + varname );
                else
                   pw.println( "c c " + v + " " + varname );
            }
        }
        catch ( Exception e ) {
            Util.fatalError( "dumpVariablesInOrder Exception " +
                                                        e.getMessage() );
        }
    }
   public int rank = -1;

    public static String findVar( int num ) {
        Iterator i = Vtable.values().iterator();
        while ( i.hasNext() ) {
            variable v = ( variable ) i.next();
            if ( v.number == num )
                return v.name;
        }
        return null;
    }
}
