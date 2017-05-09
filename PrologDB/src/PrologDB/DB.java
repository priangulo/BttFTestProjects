package PrologDB;

import PrologScanner.ParseException;
import PrologScanner.dScan;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DB extends Parse {

    /**
     * reads and parses a prolog database from File infile. may throw
     * parseExceptions or Errors, which terminates processing. Errors are
     * reported to System.err.
     *
     * @param infile the File of the Prolog database
     * @return Prolog database object of type DB
     */
    public static DB readDataBase(File infile) {
        return DB.readDataBase(infile, System.err);
    }

    /**
     * reads and parses a prolog database from given string filename. may throw
     * parseExceptions or Errors, which terminates processing. Errors are
     * reported to System.err.
     *
     * @param localFileName is the name of the file of the Prolog database
     * @return Prolog database object of type DB
     */
    public static DB readDataBase(String localFileName) {
        try {
            File in = new File(localFileName);
            return readDataBase(in);
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * reads and parses a prolog database from given string filename. may throw
     * parseExceptions or Errors, which terminates processing. Errors are
     * reported to Printstream out.
     *
     * @param infile is the File of the Prolog database
     * @param out is the PrintStream (e.g., System.out) to report errors.
     * @return Prolog database object of type DB
     */
    public static DB readDataBase(File infile, PrintStream out) {
        DB db;
        LineNumberReader br;
        String filename = infile.getName();
        String[] parts = filename.split("/");
        String[] subparts = parts[parts.length - 1].split("\\.");
        if (!subparts[2].equals("pl")) {
            throw new RuntimeException("file name " + filename + " not in <dbname>.<schemaname>.pl");
        }
        String dbname = subparts[0];
        // read the file in the first pass: getValue its schema
        DBSchema dbs = DBSchema.readSchema(infile, out);
        dbs.setFlattened();  // any database that is read has a flattened schema

        db = new DB(dbname, dbs);
        String line;
        int lineno = 1;
        try {
            // Step 1: Read infile by line
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(infile)));

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("tuple(") || line.startsWith("dbase(") || line.contains(":-") || line.startsWith("table(")
                        || line.startsWith("subtable(")
                        || line.startsWith("/*") || line.startsWith("%")) {
                    continue;
                }

                parseTupleDecl(line, br.getLineNumber(), db); // if not tuple line, ignore
            }
            br.close();
        } catch (Exception e) {
            String err = "error on line '" + lineno + " in file " + infile.getName() + "\n" + e.getMessage();
            if (Parse.Debug) {
                e.printStackTrace();
            }
            throw new Error(err);
        }
        return db;
    }

    /**
     * parses a line at lineno, expecting to find a legal prolog tuple
     * declaration. The extracted tuple is inserted into database db; the
     * possible table declarations are defined in db.getSchema() of the database
     *
     * @param line to parse
     * @param lineno line number of the line
     * @param db database in which to insert
     *
     */
    public static void parseTupleDecl(String line, int lineno, DB db) {
        // Step 1: parse tuple
        dScan ds = new dScan(line, lineno);
        ds.parser(dScan.tupleStmt);  // ignore output

        //Step 2: extract the database schema
        DBSchema dbs = db.getSchema();

        // Step 2: unpack parsing to getValue table name, and list of values, and create an empty tuple
        LinkedList<String> list = ds.parseList;
        String tableName = list.removeFirst();  // this is the table name
        Table tbl = db.findTableEH(tableName);
        TableSchema tableDef = db.getTableSchema(tableName);
        Tuple newTuple = new Tuple(tableDef);

        if (list.size() != tableDef.size()) {
            throw new ParseException(lineno, "insufficient number of values for a tuple of table " + tableDef.getName());
        }
        // Step 3: assemble the tuple
        for (int i = 0; i < tableDef.size(); i++) {
            // Get the value delimited by "@@@"
            String value = list.removeFirst(); // this is a column value
            value = value.replace("'", "");     // remove single quotes
            Column target = tableDef.getIndex(i);
            String columnName = target.getName().replace("\"", "");
            newTuple.addColumnValue(columnName, value);
        }

        // Step 4: add the tuple to the table
        tbl.addTuple(newTuple);
    }

    /**
     * the name of the database; a map of (tableNames,Tables) and the DBSchema
     * that this database instantiates
     */
    private String name;
    private final Map<String, Table> tables;
    private final DBSchema schema;

    /**
     * Create a prolog database with a given name that instantiates db schema
     *
     * @param name of the database
     * @param schema of the database
     */
    public DB(String name, DBSchema schema) {
        this.name = name;
        if (!schema.isFlattened()) {
            DBSchema copy = schema.copy();
            copy.flatten();
            schema = copy;
        }
        this.schema = schema;
        tables = new HashMap<>();
        for (TableSchema t : schema.getTableSchemas()) {
            tables.put(t.getName(), new Table(t));
        }
    }

//    public DB copy() {
//        DB dup = new DB(name, schema);
//        dup.tables.putAll(tables);  // don't think this works -- must replicate tables
//        return dup;
//    }
//    /**
//     * adds table t to 'this' database
//     *
//     * @param t table to add to the database
//     * @return updated database
//     *
//     * not currently used.
//     */
//    public DB addTable(Table t) {
//        tables.put(t.getName(), t);
//        return this;
//    }
    /**
     * return name of DB schema that this database instantiates a delegate
     * method
     *
     * @return
     */
    public String getSchemaName() {
        return schema.getName();
    }

    /**
     * returns DBSchema that this database instantiates
     *
     * @return
     */
    public DBSchema getSchema() {
        return schema;
    }

    /**
     * returns list of subtable declarations for this database's schema a
     * delegate method
     *
     * @return
     */
    public List<SubTableSchema> getSubTableSchemas() {
        return schema.getSubTableSchemas();
    }

    /**
     * returns a collection of tables that constitute this database
     *
     * @return
     */
    public Collection<Table> getTables() {
        return tables.values();
    }

    /**
     * returns the TableSchema of the table with given name; null if table is
     * not found
     *
     * @param name name of desired table
     * @return either TableSchema of this table or null
     */
    public TableSchema getTableSchema(String name) {
        return schema.findTableSchema(name);
    }

    /**
     * @return name of this database
     */
    public String getName() {
        return name;
    }

    /**
     * assigns a new name to this database
     *
     * @param name new name
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * the full name of a database is "databaseName"."schemaName"."pl" this is
     * the assumed or standard name for a prolog database file.
     *
     * @return
     */
    public String getFullName() {
        return String.format("%s.%s.pl", name, getSchema().getName());
    }

    /**
     * print in standard way the contents of a Prolog database to out
     * PrintStream
     *
     * @param out destination of printing
     */
    public void print(PrintStream out) {
        out.format(":-style_check(-discontiguous).\n\n");
        out.format("%s\n", schema.printHeader());

        for (TableSchema s : schema.getTableSchemas()) {
            Table t = tables.get(s.getName());
            t.print(out);
        }

        for (SubTableSchema sts : schema.getSubTableSchemas()) {
            sts.print(out);
        }
    }

    /**
     * ********* Find utilities *****************
     */
    /**
     * return the Table object for the table with name tableName
     *
     * @param tableName -- name of table to return
     * @return Table object or null
     */
    public Table findTable(String tableName) {
        return tables.get(tableName);
    }

    /**
     * error handler of findTable(String) return the Table object for the table
     * with name tableName
     *
     * @param tableName -- name of table to return
     * @return Table object or throws Error
     */
    public Table findTableEH(String tableName) {
        Table t = tables.get(tableName);
        if (t == null) {
            throw new Error("cannot find table " + tableName + " in database " + schema.getName());
        }
        return t;
    }

    /**
     * checks to see if there is a table with name in the database
     *
     * @param name of table
     * @return true if table is present, false otherwise
     */
    public boolean containsTable(String name) {
        return tables.containsKey(name);
    }

    /**
     * returns first tuple from table with given tablename that satisfies column
     * = value. Usually this method is called for a primary key search, where
     * the key is assumed to exist
     *
     * @param tableName name of table to search
     * @param column name of column to examine
     * @param value desired value of column
     * @return tuple or throws an error
     */
    public Tuple findFirst(String tableName, String column, String value) {
        Table tbl = findTableEH(tableName);
        return findFirst(tbl, column, value);
    }

    /**
     * returns first tuple from table with given tablename that satisfies column
     * = value. Usually this method is called for a primary key search, where
     * the key is assumed to exist
     *
     * @param table to search
     * @param column name of column to examine
     * @param value desired value of column
     * @return
     */
    public Tuple findFirst(Table table, String column, String value) {
        TableSchema ts = schema.findTableSchema(table.getName());
        List<String> names = schema.subtablesOf(ts);
        for (String tname : names) {
            Table tbl = this.findTable(tname);
            for (Tuple t : tbl.getTuples()) {
                if (t.getValue(column).equals(value)) {
                    return t;
                }
            }
        }
        throw new Error("can't find tuple in relation " + schema.getName() + " where " + column + "=" + value);
    }

    /**
     * return the list of ALL tuples from the table with name tablename that
     * satisfies column = value. All subtables of this table will be searched.
     *
     * @param tableName of the table to search
     * @param column name of the column to examine
     * @param value desired value of column
     * @return
     */
    public List<Tuple> find(String tableName, String column, String value) {
        Table t = findTableEH(tableName);
        return find(t, column, value);
    }

    /**
     * return the list of ALL tuples from the table with name tablename that
     * satisfies column = value. All subtables of this table will be searched.
     *
     * @param table to search
     * @param column name of the column to examine
     * @param value desired value of column
     * @return
     */
    public List<Tuple> find(Table table, String column, String value) {
        List<String> list = schema.subtablesOf(table.getName());
        List<Tuple> result = new LinkedList<>();
        for (String s : list) {
            Table t = findTable(s);
            result.addAll(t.find(column, value));
        }
        return result;
    }

    /**
     * return the list of ALL tuples from the table with name tableName; there
     * are no qualifications. Tuples from all subtables of this table will be
     * returned AND trimmed of unnecessary columns.
     *
     * @param tableName name of table
     * @return list of all column-projected tuples in table and its subtables
     */
    public List<Tuple> getTuples(String tableName) {
        Table tbl = findTableEH(tableName);
        return getTuples(tbl);
    }

    /**
     * return the list of ALL tuples from the table with name tableName; there
     * are no qualifications. Tuples from all subtables of this table will be
     * returned AND trimmed of unnecessary columns.
     *
     * @param table whose tuples are to be returned
     * @return list of all column-projected tuples in table and its subtables
     */
    public List<Tuple> getTuples(Table table) {
        List<String> list = schema.subtablesOf(table.getName());
        TableSchema ts = table.getSchema();
        List<Tuple> result = new LinkedList<>();
        for (String s : list) {
            Table t = findTable(s);
            for (Tuple tp : t.getTuples()) {
                result.add(tp.project(ts));
            }
        }
        return result;
    }

    /**
     * this method takes a produced list of tuples and applies a predicate,
     * column = value, to eliminate those tuples that do not satisfy it
     *
     * @param using the existing tuple list
     * @param column the name of the column
     * @param value the desired value of that column
     * @return
     */
    static public List<Tuple> find(List<Tuple> using, String column, String value) {
        LinkedList<Tuple> list = new LinkedList<>();

        for (Tuple t : using) {
            String tvalue = t.getValue(column);
            if (tvalue.equals(value)) {
                list.add(t);
            }
        }
        return list;
    }
}
