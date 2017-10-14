package guidsl;
import java.util.ArrayList;
public class and extends node {
    public    and( node l, node r ) {
        left = l;
        right = r;
    }

    public    node klone() {
        return new and( left.klone(),right.klone() );
    }

    public    node simplify() {
        // simplify both sides of an and
        left = left.simplify();
        right = right.simplify();
        return this;
    }

    public String toString() {
        return array2String( children().toArray(), " and " );
    }

    public ArrayList children() {
        ArrayList ll;
        ArrayList l = new ArrayList();
        if ( left instanceof and )
            l.addAll( ( ( and ) left ).children() );
        else
            l.add( left );
        if ( right instanceof and)
            l.addAll( ( ( and ) right ).children() );
        else
            l.add( right );
        return l;
    }
    public    String cnf2String() {
        // return parentheses around non-and arguments
        //
        String result;
        if ( left instanceof and )
            result = left.cnf2String();
        else
            result = "(" + left.cnf2String() + ")";
        if ( right instanceof and )
            result = result + " and " +  right.cnf2String();
        else
            result = result +  " and (" + right.cnf2String() + ")";
        return result;
    }

    public    node cnf() {
        // rule: anything can sit immediately below ands
        //
        left = left.cnf();
        right = right.cnf();
        return this;
    }
   public void reduce( ArrayList terms ) {
     left.reduce(terms);
     right.reduce(terms);
   }

    boolean oktype( node n ) {
        return ( n instanceof not || n instanceof bterm || n instanceof or );
    }

    public void toCnfFormat( cnfout out ) throws CNFException {
        // convert the left argument -- add eol if something reasonable
        // sits below
 
        out.andSeen();

        left.toCnfFormat( out );
        if ( oktype( left ) ) {
            out.println( " 0" );
            out.inc();
        }

        // now do the same for the right...

        right.toCnfFormat( out );
        if ( oktype( right ) ) {
            out.println( " 0" );
            out.inc();
        }
    }
    public String toXMLString() {
        StringBuffer str=new StringBuffer();
        Object obj[] = children().toArray();

        str.append("<and>");
        for(int i=0;i< obj.length;i++){
            str.append( ((node)obj[i]).toXMLString());
        }
        str.append("</and>");

        return str.toString();

    }
}
