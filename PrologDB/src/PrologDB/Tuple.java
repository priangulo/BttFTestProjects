package PrologDB;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tuple {

    /**
     * schema points to TableSchema of tuple, values is a map that pairs columns
     * with values
     */
    private final TableSchema schema;
    private final Map<String, String> values;

    /**
     * create an empty tuple for the given schema
     *
     * @param schema tuple schema to instantiate
     */
    public Tuple(TableSchema schema) {
        this.schema = schema;
        values = new HashMap<>();
    }

    /**
     * create an empty tuple for the given table
     *
     * @param table
     */
    public Tuple(Table table) {
        this(table.getSchema());
    }

    /**
     * return a copy of the tuple; the (column,value) pairs are shared with
     * 'this' tuple as these pairs do not point to their enclosing tuple
     *
     * @return
     */
    public Tuple copy() {
        Tuple cpy = new Tuple(schema);
        cpy.values.putAll(values);
        return cpy;
    }

    /**
     * this is the counterpart to TableSchema join; this method joins a list of
     * tuples into a tuple for TableSchema tableSchema An assertion is checked
     * to ensure that all columns in the tuple are accounted for in the
     * tableSchema; an Error is thrown otherwise
     *
     * @param tableSchema is schema of the returned tuple
     * @param tlist is the set of tuples to join/concatenate
     * @return a tuple of type tableSchema
     */
    public Tuple join(TableSchema tableSchema, Tuple... tlist) {
        Tuple t = new Tuple(tableSchema);

        for (Tuple tup : tlist) {
            t.loadem(tup);
        }
        t.isComplete();
        return t;
    }

    private void loadem(Tuple t) {
        String schemaPrefix = t.getSchema().getName() + "_";
        for (Column c : t.getColumns()) {
            String colName = c.getName();
            addColumnValue(schemaPrefix + colName, t.getValue(colName));
        }
    }

    /**
     * adds a list of values, in order in which their columns are defined, to an
     * empty tuple. an Error is thrown if not all columns have values.
     *
     * @param vals is list of values to add to an empty tuple
     */
    public void addColumnValuesEH(String... vals) {
        if (vals.length != schema.size()) {
            throw new Error("adding " + vals.length + " column values to table " + schema.getName() + " with " + schema.size() + "columns");
        }
        int i = 0;
        for (Column c : schema.getColumns()) {
            values.put(c.getName(), vals[i]);
            i++;
        }
        isComplete();
    }

    /**
     * adds an individual column,value pair to a tuple; Error thrown if column
     * is not existent
     *
     * @param name of column
     * @param value value of column
     * @return updated tuple
     */
    public Tuple addColumnValue(String name, String value) {
        schema.findColumnEH(name);  // throws error if non-existent
        values.put(name, value);
        return this;
    }

    /**
     * return list of column definitions of the tuple this method delegates to
     * tableschema.getColumns();
     *
     * @return list of column definitions of the tuple
     */
    public List<Column> getColumns() {
        return schema.getColumns();
    }

    /**
     * returns value of given column columnName
     *
     * @param columnName
     * @return
     */
    public String getValue(String columnName) {
        return values.get(columnName);
    }

    /**
     * returns TableSchema of 'this' tuple
     *
     * @return TableSchema of 'this' tuple
     */
    public TableSchema getSchema() {
        return schema;
    }

    /**
     * returns current number of (column,value) pairs in this tuple. When tuple
     * isComplete(), this should be the same number as the number of columns in
     * its tuple schema
     *
     * @return
     */
    public int size() {
        return values.size();
    }

    /**
     * projects 'this' tuple to the set of columns of a super table schema
     *
     * @param suprTableSchema
     * @return column-projected tuple
     */
    public Tuple project(TableSchema suprTableSchema) {
        Tuple t = new Tuple(suprTableSchema);
        int ncols = suprTableSchema.size();
        for (Column c : schema.getColumns()) {
            String colName = c.getName();
            String value = getValue(colName);
            t.addColumnValue(colName, value);
            if (--ncols <= 0) {
                break;
            }
        }
        return t;
    }

    /**
     * projects a list of tuples to the set of columns of a super table schema
     *
     * @param list of tuples to project
     * @param suprTableSchema
     * @return column-projected list of tuples
     */
    public List<Tuple> project(List<Tuple> list, TableSchema suprTableSchema) {
        LinkedList<Tuple> newList = new LinkedList<>();
        for (Tuple t : list) {
            newList.add(t.project(suprTableSchema));
        }
        return list;
    }

    // does tuple have a full complement of values?
    public void isComplete() {
        String errors = "";
        for (Column c : schema.getColumns()) {
            String columnName = c.getName();
            if (!values.containsKey(columnName)) {
                errors += " " + columnName;
            }
        }
        if (!errors.equals("")) {
            throw new Error("tuple of " + schema.getName() + " missing values for columns" + errors);
        }
        for (String colName : values.keySet()) {
            if (schema.findColumnEH(colName) == null) {
                errors += " " + colName;
            }
        }
        if (!errors.equals("")) {
            throw new Error("tuple of " + schema.getName() + " has too many columns " + errors);
        }
    }

    /**
     * print tuple to PrintStream out
     *
     * @param out where to print
     */
    public void print(PrintStream out) {
        if (values.size() != schema.size()) {
            throw new Error("tuple from table " + schema.getName() + " does not have correct number of attributes");
        }
        String q;
        String tuple = schema.getName() + "(";
        String comma = "";
        for (Column c : schema.getColumns()) {
            q = c.isQuoted() ? "'" : "";
            String val = values.get(c.getName());
            if (val == null && !c.isQuoted()) {
                val = "null";
            }
            tuple = tuple + comma + q + val + q;
            comma = ",";
        }
        tuple = tuple + ").\n";
        out.format(tuple);
    }

    /**
     * print list of tuples (with an optional string title) to PrintStream out
     *
     * @param list of tuples to print
     * @param title a string that is printed to distinguish this list
     * @param out where to print
     */
    public static void print(List<Tuple> list, String title, PrintStream out) {
        if (title != null) {
            out.format("%s\n", title);
        }
        for (Tuple t : list) {
            t.print(out);
        }
        if (title != null) {
            out.format("\n");
        }
    }
}
