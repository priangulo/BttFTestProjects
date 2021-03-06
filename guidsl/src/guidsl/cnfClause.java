package guidsl;import Jakarta.util.*;
import java.util.*;
import javax.swing.*;

public class cnfClause {
   public static ArrayList clist = new ArrayList();  // List of all cnfClauses

   public ArrayList terms;
   node      formula;
   String    formulaStr;

   public cnfClause( ) {
      terms = new ArrayList();
      formula = null;
      formulaStr = null;
   }

   static void setFormula( ArrayList terms, node cnf ){
      String s = cnf.toString();

      Iterator i = terms.iterator();
      while (i.hasNext()) {
         cnfClause t = ( cnfClause ) i.next();
         t.formula = cnf;
         t.formulaStr = s;
      }
   }

   void add( cterm c ) { terms.add(c); }

   void setFormula( node cnf ) {
      formula = cnf;
      formulaStr = cnf.toString();
   }

   static void dumpCList() {
      System.out.println("Dumping clist");
		Iterator i = clist.iterator();
		int cnt = 0;
		while (i.hasNext()) {
         cnfClause c = ( cnfClause ) i.next();
         System.out.print(cnt++ + " ");
         c.print();
      }
      System.out.println();
   }

    static String ctStr = "";
    static String reasonOut = "";
    cterm isUnitOpen() {
        cterm openCterm = null;
        Iterator i = terms.iterator();
        while ( i.hasNext() ) {
            cterm t = ( cterm ) i.next();
            switch ( t.eval3() )
            {
                case variable.F:
                // do nothing
                break;
                case variable.T:
                // can't be open
                return null;
                case variable.U:
                if ( openCterm == null )
                    openCterm = t;
                else // more than one term unknown -- can't be open
                    return null;
                break;
            }
        }
        // if we get this far, this is a unit open clause or a unsatisfied clause
        return openCterm;
    }

    boolean isViolated() {
        // all cterms must be false
        Iterator i = terms.iterator();
        while ( i.hasNext() ) {
            cterm t = ( cterm ) i.next();
            if ( t.eval3() != variable.F ){
                return false;
            }
        }
        return true;
    }

    // does this clause have a negated term for a variable
    boolean hasNegTerm( boolean neg, variable v ) {
        Iterator i = terms.iterator();
        while ( i.hasNext() ) {
            cterm t = ( cterm ) i.next();
            if ( neg &&  t.negated && t.var == v )
                return true;
            if (!neg && !t.negated && t.var == v )
                return true;
        }
        return false;
    }

    static public Stack stack;

    // BCP algorithm
    static void BCP() {
        while ( !stack.empty() ) {
            cnfClause c = ( cnfClause ) stack.pop();
            cterm t = c.isUnitOpen();
            if ( t != null ) {
                t.var.set(t.negated);
                t.var.justify(c);
            }
        }
    }

   // propagate constant constraints (i.e., constraints of
   // a single variable) in the model

   static void propagateConstants() {
      Iterator i = clist.iterator();
      while (i.hasNext()) {
         cnfClause c = ( cnfClause ) i.next();
         c.propagateConstant();
      }
   }

   void propagateConstant() {
      // first, see if there is but one variable per clause

      cterm t = null;
      Iterator i = terms.iterator();
      while ( i.hasNext() ) {
          if (t != null) return;
          t = ( cterm ) i.next();
      }

      // we are here if there is only one variable.

      variable v = t.var;
      boolean  b = t.negated;
      v.set(b);
      v.justify(this);
      v.modelSet = true;
      BCP();
   }

/************ Model Checker functions **************/
    // BCP algorithm
    static boolean MC_BCP() {
        while ( !stack.empty() ) {
            cnfClause c = ( cnfClause ) stack.pop();
            cterm t = c.isUnitOpen();
            if ( t != null ) {
                t.var.justify(c);
                if(!t.var.setNoDialog(t.negated)){
                    System.out.println(t.var.name);
                    reasonOut = t.var.name + " has a contradiction" + "\n" +
                                       t.var.explainValue() +
                                       "But " + ctStr;
                    System.out.println(reasonOut);
                    return false;
                }
                //t.var.justify(c);
            }
        }
        return true;
    }

   // propagate constant constraints (i.e., constraints of
   // a single variable) in the model

   static boolean MC_propagateConstants() {
      Iterator i = clist.iterator();
      while (i.hasNext()) {
         cnfClause c = ( cnfClause ) i.next();
         if(!c.MC_propagateConstant())
            return false;
      }
      return true;
   }

   boolean MC_propagateConstant() {
      // first, see if there is but one variable per clause

      cterm t = null;
      Iterator i = terms.iterator();
      while ( i.hasNext() ) {
          if (t != null) return true;
          t = ( cterm ) i.next();
      }

      // we are here if there is only one variable.

      variable v = t.var;
      boolean  b = t.negated;
      if(!v.setNoDialog(b)){
        v.justify(this);
        return false;
      }
      v.justify(this);
      v.modelSet = true;
      if(!MC_BCP())
        return false;
      return true;
   }

/****** end of Model Checker functions ***********/

   void print() {
      System.out.print("cnf clause: ");
      Iterator i = terms.iterator();
      while ( i.hasNext() ) {
          cterm t = (cterm) i.next();
          t.print();
      }
      System.out.println("");
   }

   static boolean complete(boolean output) {
      // in 2-valued logic, a complete specification is where
      // all clauses are satisfied

      Iterator i = clist.iterator();
      int cnt = 0;
      String completeMsg = null;

      while (i.hasNext()) {
         cnfClause c = ( cnfClause ) i.next();
         if (!c.eval2()) {
            String msg = c.formula.incompleteMessage;
            if (msg == null)
               msg = c.formulaStr;
            if (completeMsg == null)
               completeMsg = msg;
            else
               completeMsg += "\n" + msg;
         }
      }
      if (completeMsg == null)
         return true;
        if (output)
         JOptionPane.showMessageDialog( null, completeMsg,
         "Specification Incomplete!", JOptionPane.ERROR_MESSAGE );
      return false;
   }

   // 2-value logic evaluation of a clause -- if any term is
   // true, then the clause is true

   boolean eval2() {
      Iterator i = terms.iterator();
      while ( i.hasNext() ) {
         cterm t = ( cterm ) i.next();
         if (t.eval2())
            return true;
      }
      return false;
   }

   // returns arraylist of variables whose values are false

   ArrayList getAntecedents( variable v ) {
      ArrayList a = new ArrayList();
      Iterator i = terms.iterator();
      while (i.hasNext()) {
         variable vv = (variable) i.next();
         if (vv == v) continue;
         a.add(vv);
      }
      return a;
   }
}
