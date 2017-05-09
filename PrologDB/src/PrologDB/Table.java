package PrologDB;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Table {

    /**
     * name of table, schema of table, and a list of Tuples of this table that
     * conform to the TableSchema
     */
    protected final String name;
    protected final TableSchema schema;
    private final List<Tuple> tuples;

    /**
     * create an empty table for TableSchema schema
     *
     * @param schema
     */
    public Table(TableSchema schema) {
        this.name = schema.getName();
        this.schema = schema.copy();
        tuples = new ArrayList<>();
    }

    /**
     * return a copy of this Table; the copy shares the tuples of the original
     * table
     *
     * @return copy of the Table object, not its tuples
     */
    public Table copy() {
        Table dup = new Table(schema);
        dup.tuples.addAll(tuples);
        return dup;
    }

    /**
     *
     * @return name of table
     */
    public String getName() {
        //String result = (name == null) ? schema.getName() : name;
        return name;
    }

    /**
     *
     * @return schema of table
     */
    public TableSchema getSchema() {
        return schema;
    }

    /**
     * @return list of columns of the table delegates to getColumns of
     * TableSchema
     */
    public List<Column> getColumns() {
        return schema.getColumns();
    }

    /**
     * @return list of Tuples of this table
     */
    public List<Tuple> getTuples() {
        return tuples;
    }

    /**
     * returns the list of tuples of this table that are truncated/projected to
     * the columns of super table schema supr
     *
     * @param supr
     * @return list of column-projected tuples of this table
     */
    public List<Tuple> getTuples(TableSchema supr) {
        LinkedList<Tuple> list = new LinkedList<>();
        for (Tuple t : tuples) {
            list.add(t.project(supr));
        }
        return list;
    }

    /**
     *
     * @return number of tuples in this table
     */
    public int size() {
        return tuples.size();
    }

    /**
     * adds tuple t to 'this' table; throws error if # of columns does not match
     * that of schema declaration
     *
     * @param t is tuple to add
     */
    public void addTuple(Tuple t) {
        if (t.size() != schema.size()) {
            throw new Error("tuple from table " + schema.getName() + " does not have correct number of attributes");
        }
        tuples.add(t); // append
    }

    /**
     * finds first tuple in this table (not subtables) where column = value
     * otherwise throws Error if one not found. Usually this method is called to
     * retrieve a tuple via its identifier
     *
     * @param column name of predicate
     * @param value that column should have
     * @return first tuple of this table to satisfy predicate, otherwise throw
     * an Error
     */
    public Tuple findFirst(String column, String value) {
        for (Tuple t : tuples) {
            if (t.getValue(column).equals(value)) {
                return t;
            }
        }
        throw new Error("can't find tuple in relation " + schema.getName() + " where " + column + "=" + value);
    }

    /**
     * return list of all tuples of this table (not subtables) where column =
     * value
     *
     * @param column name of predicate
     * @param value that column should have
     * @return list of all tuples of this table (not subtables) where column =
     * value
     */
    public List<Tuple> find(String column, String value) {
        return find(tuples, column, value);
    }

    /**
     * delegate method to DB.find(using,column,value)
     *
     * @param using is list of tuples to qualify
     * @param column name of predicate
     * @param value desired value of column
     * @return predicate shortened list
     */
    public List<Tuple> find(List<Tuple> using, String column, String value) {
        return DB.find(using, column, value);
    }

    /**
     * print table to PrintStream out
     *
     * @param out
     */
    public void print(PrintStream out) {
        if (schema == null) {
            throw new Error("schema not set in table " + name);
        }
        schema.print(out);
        for (Tuple t : tuples) {
            t.print(out);
        }
        if (tuples.isEmpty()) {
            out.format(":- dynamic %s/%d.\n", schema.getName(), schema.size());
        }
        out.format("\n");
    }
}
