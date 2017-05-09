package PrologDB;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class TableSchema {

    /**
     * name of table schema, and list of column declarations
     */
    private final String name;
    private final List<Column> columns;

    /**
     * create table schema with name and empty list of columns
     *
     * @param name of new TableSchema
     */
    public TableSchema(String name) {
        this.name = makeFirstLowerCase(name);
        columns = new ArrayList<>();
    }

    /**
     * create table schema with name and set of columns
     *
     * @param name of new TableScheam
     * @param cols is list of column declarations
     */
    public TableSchema(String name, List<Column> cols) {
        this.name = name;
        columns = new ArrayList<>();
        columns.addAll(cols);
    }

    /**
     * @return duplicate of this TableSchema columns are shared with replicate
     * (as columns don't point back to their parent Schemas, and thus can be
     * shared
     */
    public TableSchema copy() {
        TableSchema dup = new TableSchema(name, columns);
        return dup;
    }

    /**
     * concatenate/join 'this' table schema with a second (s) to produce a new
     * Tableschema
     */
    public TableSchema join(TableSchema s) {
        TableSchema newts = new TableSchema(name + "_x_" + s.getName());
        newts.addKols(this);
        newts.addKols(s);
        return newts;
    }

    private void addKols(TableSchema ts) {
        String namePrefix = ts.getName() + "_";
        for (Column c : ts.getColumns()) {
            String newColumnName = namePrefix + c.getName();
            addColumn(new Column(newColumnName, c.isQuoted()));
        }
    }

    /**
     * create a new Table instance of this schema. The name of the table = the
     * name of the schema
     *
     * @return
     */
    public Table instantiate() {
        return new Table(this);
    }

    /*
     * @return name of table schema
     */
    public String getName() {
        return name;
    }

    /**
     * @param i column index
     * @return column i of the schema; can throw runtime exception if i is out
     * of bounds
     */
    public Column getIndex(int i) {
        return columns.get(i);
    }

    /**
     * @return list of columns of a tableschema
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * @return number of columns in schema
     */
    public int size() {
        return columns.size();
    }

    /**
     * add a new column c to 'this' TableSchema
     *
     * @param c is column to add
     */
    public void addColumn(Column c) {
        addColumn(c, true);
    }

    /**
     * add column c to 'this' schema; if append is true, c is added to the end
     * of the list; if false, c is added to the front
     *
     * @param c column to add
     * @param append add c to the end of the column list
     */
    public void addColumn(Column c, boolean append) {
        for (Column k : columns) {
            if (k.getName().equals(c.getName())) {
                throw new Error("multiple columns with name " + c.getName() + " in table " + name);
            }
        }
        if (append) {
            columns.add(c); // append
        } else {
            columns.add(0, c); // put at front
        }
    }

    /**
     * add a list of columns, columnList, to 'this' schema; if append is true,
     * columnList is added to the end of 'this' column list; if false, it is
     * added to the front
     *
     * @param columnList is list of columns to add
     * @param append is true if columnList is to be appended
     */
    public void addColumns(List<Column> columnList, boolean append) {
        for (Column k : columnList) {
            addColumn(k, append);
        }
    }

    /**
     * @param name of column to find
     * @return column with name; Error thrown if not found
     */
    public Column findColumnEH(String name) {
        Column c = findColumn(name);
        if (c == null) {
            throw new Error("can't find column " + name + " in table " + this.name);
        } else {
            return c;
        }
    }

    /**
     *
     * @param name of column
     * @return column with name; null if not found
     */
    public Column findColumn(String name) {
        for (Column c : columns) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * print 'this' TableSchema to PrintStream out
     *
     * @param out where to print
     */
    public void print(PrintStream out) {
        String q;
        String tb = "table(" + name + ",[";
        String comma = "";
        for (Column c : columns) {
            q = c.isQuoted() ? "\"" : "";
            tb = tb + comma + q + c.getName() + q;
            comma = ",";
        }
        tb = tb + "]).\n";
        out.format(tb);
    }

    /**
     * for writing MDELite tuple definitions
     *
     * @param out
     */
    public void writeTupleDef(PrintStream out) {
        // Step 1: create a list of prolog variable, the # of which mustEqual the # of attributes in the relation
        String accum = varList(0);

        // Step 2: output the tuple definition
        out.format("tuple(%s,L):-%s(%s),L=[%s].\n", name, name, accum, accum);
    }

    public void printInheritanceRule(PrintStream out) {
        String pList = varList(0);
        out.format("%sALL(%s):-%s(%s).\n", getName(), pList, getName(), pList);
    }

    // create a list of prolog variables, the # of which mustEqual the # of attributes in the relation
    String varList(int fill) {
        int len = columns.size();
        String accum = "";
        String comma = "";
        char letter = 'A';
        for (int i = 1; i <= (len - fill); i++) {
            accum = accum + comma + letter + i;
            comma = ",";
        }
        for (int j = 1; j <= fill; j++) {
            accum = accum + ",_";
        }
        return accum;
    }

    private static String makeFirstLowerCase(String s) {
        String tail = s.substring(1);
        String head = s.substring(0, 1).toLowerCase();
        return head + tail;
    }

    /**
     * assertion that 'this' schema must equal otherSchema Error is thrown
     * otherwise
     *
     * @param otherSchema
     */
    public void mustEqual(TableSchema otherSchema) {
        if (otherSchema == null) {
            throw new Error("otherschema is null");
        }
        if (columns.size() != otherSchema.columns.size()) {
            throw new Error("table schemas (" + name + "," + otherSchema.getName() + ") do not have same # of columns");
        }
        if (!name.equals(otherSchema.getName())) {
            throw new Error("table schemas (" + name + "," + otherSchema.getName() + ") do not have same names");
        }
        for (Column c : columns) {
            c.mustEqual(otherSchema.findColumnEH(c.getName())); // throw error if not equal
        }
    }
}
