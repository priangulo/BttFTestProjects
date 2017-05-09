package guidsl;import java.util.*;
import Jakarta.util.*;import javax.swing.*;
import java.awt.*;

public class pattern extends gObj {
    static int counter = 0;
    static pattern current;
    public static HashMap Ttable = null;  // contains all patterns
    public ArrayList terms;
    public production myproduction;

    pattern( String name ) {
      super( name );
      current = (pattern) this;
      terms = new ArrayList();
      production.current.pat.add( (pattern) this );
      myproduction=production.current;
      if (counter == 0)
        Ttable = new HashMap();
      Ttable.put( name, (pattern) this );
      counter++;
    }

    public void visit( GVisitor v ) {
        v.action( (pattern) this );
    }

    public void traverse( GVisitor v ) {
        Iterator i = terms.iterator();
        while ( i.hasNext() ) {
            term t = ( term ) i.next();
            t.visit( v );
        }
    }

    public static void dumpTtable() {
        pattern p;
        int cnt = 0;
        System.out.println( "-------Begin Ttable Dump----------" );
        Iterator i = Ttable.values().iterator();
        while ( i.hasNext() ) {
            p = ( pattern ) i.next();
            p.print();
            System.out.println();
            cnt++;
        }
        System.out.println( cnt + " patterns in all." );
        System.out.println( "-------End Ttable Dump----------" );
    }

    node formula;

   static void makeFormula() {
      Iterator i = Ttable.values().iterator();
      while ( i.hasNext() ) {
         pattern p = ( pattern ) i.next();
         p.formula = p.makef();
      } 
   }

   node makef( ) {
      node n = null;
      Iterator i = terms.iterator();
      while ( i.hasNext() ) {
         term t = ( term ) i.next();
         node tn = null;
         if (t instanceof optprim || t instanceof optprod || t instanceof star)
            tn = new implies( new bterm( t.name ), new bterm( name ));
         else
            tn = new iff( new bterm(name), new bterm( t.name ));
         if (n == null)
            n = tn;
         else
            n = new and( n, tn );
      }
      return n;
   }

   public void print() {
      String v = "null";
	   if (var!=null) v = var.name; 
	   System.out.print(" " +name + " var is " + v );
      System.out.print( " formula = " + formula ); 
   }
   node simple;
   node cnf;

   static void makeClauses() {
      if (Main.outputModelPredicate) {
         System.out.println();
         System.out.println("// Pattern");
      }

      Iterator i = Ttable.values().iterator();
      while ( i.hasNext() ) {
         pattern p = ( pattern ) i.next();
         p.simple = p.formula.klone().simplify();
         if (Main.outputModelPredicate)
            System.out.println(p.simple + " and");  /* dsb */
         p.cnf    = p.simple.klone().cnf();
         ArrayList al = new ArrayList();
         p.cnf.reduce(al);
         cnfClause.setFormula(al, p.formula);
         cnfClause.clist.addAll(al);
      }
   }

    public JComponent draw (int several) {
       // Step 1: create horizontal panel and add terms that
       //         have a non-null graphic
   JPanel panel = new JPanel();
    panel.setLayout( new FlowLayout(FlowLayout.LEFT) );
   // panel.setBorder( BorderFactory.createEtchedBorder()); //no panel required, spoils the visual

   if (var==null)
           Util.fatalError(" var null for " + name);

       // Step 2: add terms with non-null graphic
       Iterator i = terms.iterator();
        while ( i.hasNext() ) {
          term t = (term) i.next();
          JComponent j = t.draw(several);

          if (j!=null)
             panel.add( t.setWidget(j) );
       }
        return panel;
    }

    /*
    returns true if pattern has non-optional subterms
    used by production when deciding whether to display
    a checkbox for an optional single-pattern production
    */

    boolean hasNonOpt(){
        Iterator i = terms.iterator();
        while(i.hasNext()){
            term t = (term)i.next();
            if (t.prod != null && t.prod.type != production.opt )//non-optional production
                    return true;
            else
                if(t.getClass().getName().equals("guidsl.prim"))//non-optional primitive
                    return true;
        }
        return false;
    }

    // method walks through the pattern Table, for each pattern
    // convert it into cnf formula, and then output the
    // converted formula into CNF format

    public static void toCnfFormat( cnfout out ) throws CNFException {
        Iterator i = Ttable.values().iterator();
        while ( i.hasNext() ) {
            pattern p = ( pattern ) i.next();
            node simple = p.formula.klone().simplify();
                node cnf = simple.klone().cnf();
            out.beginFormula( p.formula );
                out.comment(simple);
                out.cnfcomment(cnf);
            cnf.toCnfFormat( out );
            out.endFormula();
        }
    }
}
