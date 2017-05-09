package PrologDB;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SubTableSchema {

    /** 
     * super is the schema of parent table
     * subs is a list of schemas that are children of the parent table
     */
    TableSchema supr;
    List<TableSchema> subs;

    /**
     * create new SubTable object for parent schema supr
     * @param supr is Table object of parent schema
     */
    public SubTableSchema(TableSchema supr) {
        this.supr = supr;
        subs = new ArrayList<>();
    }
    
    /**
     * create subTableSchema declaration for parent TableSchema supr
     * with subtable Schemas subs
     * @param supr TableSchema of parent table
     * @param subs list of TableSchemas of children tables
     */
    public SubTableSchema(TableSchema supr, List<TableSchema> subs) {
        this.supr = supr;
        this.subs = subs;
    }
    
    
    private SubTableSchema() {
        supr = null;
        subs = new LinkedList<>();
    }
    
    /**
     * copy SubTableSchema by creating a new, pristine version that
     * literally replicates the data, but is specific to a new database
     * schema.
     * @param newSchema a DBSchema for cloning a subtable schema decl
     * @return copy of a SubTableSchema w.r.t. newSchema
     */
    public SubTableSchema copy(DBSchema newSchema) {
        // here's the idea --  we can't just copy a subtableschema -- we must
        // translate its supr and subs into corresponding objects of the new schema.
        
        // Step 1: create an empty copy and get the list of new subschemas
        SubTableSchema copy = new SubTableSchema();
        List<TableSchema> os = newSchema.getTableSchemas();
        
        // Step 2: translate supr into supr of new TableSchema
        copy.supr = find(supr.getName(),os);
        
        // Step 3: now translate each subschema to the new TableSchema
        for (TableSchema ts : subs) {
            copy.subs.add(find(ts.getName(),os));
        }
        
        // Step 4: return true copy
        return copy;
    }
    
    private static TableSchema find(String name, List<TableSchema> list) {
        for ( TableSchema ts : list ) {
            if (ts.getName().equals(name)) {
                return ts;
            }
        }
        throw new Error("cannot find SubTableSchema declaration for "+name);
    }


    void addSubTableSchema(TableSchema sub) {
        subs.add(sub);
    }
    
    void addSubTableSchemas(List<TableSchema> subs) {
        this.subs.addAll(subs);
    }
    
    /**
     * return subtable schemas of a SubTableSchema declaration
     * @return subtable schemas of a SubTableSchema declaration
     */
    public List<TableSchema> getSubTableSchemas() {
        return subs;
    }
    
    /**
     * return name of super Table 
     * @return 
     */
    public String getName() { 
         return supr.getName();
    }
    
    /**
     * @return return the TableSchema of super table
     */
    public TableSchema getSuper() {
        return supr;
    }

    // push attributes of super table into subtable
    
    void flatten() {
        List<Column> columns = supr.getColumns();
        for (TableSchema t : subs) {
            t.addColumns(MyList.reverse(columns),false);
        }
    }
    
    /**
     * does this subtable schema include TableSchema s as subtable?
     * @param s TableSchema of interest
     * @return true if s is a child schema of 'this'
     */
    public boolean contains(TableSchema s) {
        return subs.contains(s);
    }

    /**
     * print subtable schema on PrintStream out
     * @param out is output PrintStream
     */
    public void print(PrintStream out) {
        out.format("subtable(%s,[", supr.getName());
        String comma = "";
        for (TableSchema s : subs) {
            out.format("%s%s", comma, s.getName());
            comma = ",";
        }
        out.format("]).\n");
    }

    /**
     * method is useful for printing prolog rules for returning all tuples
     * in a Prolog table (and its subtables)
     * @param out where to print
     */
    public void printInheritanceRules(PrintStream out) {
        String pList = supr.varList(0);
        String nList;

        for (TableSchema n : subs) {
            nList = n.varList(n.size() - supr.size());
            out.format("%sALL(%s):-%sALL(%s).\n", supr.getName(), pList, n.getName(), nList);
        }
    }
}
