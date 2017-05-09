package guidsl;import Jakarta.util.*;
import java.util.*;abstract public class node {
    /**
     * left or first argument of a boolean operator
     */
    public   node left = null;

    /**
     * right or second argument of a boolean operator
     */
    public    node right = null;

    /**
     * expands all non-primitive operators (implies, etc.)
     * and returns an equivalent boolean expression
     */
    public   abstract node simplify();
    /**
     * returns string of expression 
     */
    public abstract String toString();
    /**
     * returns conjunctive-normal-form string of cnf expression
     */
    public    abstract String cnf2String();
    /**
     * returns deep copy 
     */
    public    abstract node klone();
    /**
     * converts simplified expression into conjunctive-normal-form
     * t = new ...;
     * t = t.simplify();
     * t = t.cnf();
     */
    public    abstract node cnf();

    public String array2String( Object[] o, String op ) {
        String result = "("+ ((node) o[0]).toString();
        for (int i=1; i<o.length; i++ )
           result = result + op + ((node)o[i]).toString();
        return result+")"; 
   }
   void reduce( ArrayList terms ) {
      Util.fatalError( this.getClass().getName() + ".reduce(ArrayList) called");
   }

   void reduce( cnfClause c ) {
      Util.fatalError( this.getClass().getName() + ".reduce(cnfClause) called" );
   }

   void reduce( cterm t ) {
      Util.fatalError( this.getClass().getName() + ".reduce(cterm) called" );
   }
   String incompleteMessage = null;

    // cnf file translation for most nodes throws an exception.

    public void toCnfFormat( cnfout out ) throws CNFException {
        throw new CNFException( this.getClass().getName() +".toCnfFormat() invoked" );
    }
    public abstract String toXMLString();
}
