package PrologDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBSchema extends Parse {

    /**
     * reads prolog schema from File schemafile; errors are reported to out
     *
     * @param schemafile Java File of schema file to read (can also be a
     * database file, as it too embeds schema information)
     * @param out PrintStream to report errors
     * @return DBSchema object
     */
    public static DBSchema readSchema(File schemafile, PrintStream out) {
        DBSchema dbs = null;
        List<String> tableNames = new ArrayList<>();
        try {
            LineNumberReader br = new LineNumberReader(new InputStreamReader(new FileInputStream(schemafile)));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("/*") || line.startsWith("%")) {
                    continue;
                }
                int lineno = br.getLineNumber();

                try {
                    if (line.startsWith("dbase(")) {
                        if (dbs != null) {
                            throw new Error("more than one dbase() declaration in file " + schemafile.getName());
                        }
                        dbs = parseDBaseDecl(line, lineno, tableNames);
                    } else if (line.startsWith("table")) {
                        parseTableDecl(line, lineno, dbs);
                    } else if (line.startsWith("subtable")) {
                        parseSubTableDecl(line, lineno, dbs);
                    }
                    // ignore everything else
                } catch (Exception e) {
                    String err = String.format("unable to parse table on line %d in %s\n%s\n",
                            br.getLineNumber(), schemafile.getName(), e.getMessage());
                    throw new Error(err);
                }
            }
            br.close();
            // we have parsed a database schema file
            // Let's make sure that the tables declared match what is in the dbase declaration

            for (String s : tableNames) {
                dbs.findTableSchemaEH(s); // throw error if not found
            }
            for (TableSchema ts : dbs.getTableSchemas()) {
                if (!tableNames.contains(ts.getName())) {
                    throw new Error("table " + ts.getName() + " not defined in dbase decl");
                }
            }
        } catch (IOException e) {
            if (Parse.Debug) {
                e.printStackTrace();
            }
            throw new Error(e.getMessage());
        }
        return dbs;

        // finally, depending on who is calling this, the flatten attribute is to be set.
    }

    /**
     * name of database schema list of table schemas, one per table in the
     * schema list of subtable declarations boolean indicating if schema has
     * been flattened. a schema is flattened when associated with a DB (prolog
     * database object). There is no unflattening operation, as of now.
     */
    private String name;
    private final LinkedList<TableSchema> schemas;
    private LinkedList<SubTableSchema> subtables;
    private boolean flattened = false;

    /**
     * create DBSchema that has name and a list of TableSchemas can
     * incrementally add subtable schemas subsequently -- before schema is
     * instantiated as a database
     *
     * @param name of DBSchema
     * @param schemas comma-separated list of table schemas
     */
    public DBSchema(String name, TableSchema... schemas) {
        this.name = name;
        this.schemas = new LinkedList<>();
        this.schemas.addAll(Arrays.asList(schemas));
        this.subtables = new LinkedList<>();
    }

    /**
     * This constructor is used for the incremental construction of database
     * schemas. First provide a name of the schema, then incrementally add
     * schemas per table and then subtables. By default, schema is not
     * flattened.
     *
     * @param name
     */
    public DBSchema(String name) {
        this.name = name;
        this.schemas = new LinkedList<>();
        subtables = new LinkedList<>();
    }

    /**
     * copy a database schema -- please do not alter existing table definitions
     * and subtable definitions. can add more tables, subtable definitions.
     * Copied schema is flattened.
     *
     * @return copied schema
     */
    public DBSchema copy() {
        DBSchema s = new DBSchema(name);
        s.schemas.addAll(schemas);
        s.subtables.addAll(subtables);
        s.flattened = flattened;
        return s;
    }

    /**
     * add table schema tableschema to 'this' database schema
     *
     * @param tableschema to add to 'this' database schema
     */
    public void addTableSchema(TableSchema tableschema) {
        String tableSchemaName = tableschema.getName();
        if (findTableSchema(tableSchemaName) != null) {
            throw new Error("multiple table declarations for " + tableSchemaName);  // throw error if not present
        }
        schemas.add(tableschema);
    }

    /**
     * add subtable schema sts to 'this' database schema
     *
     * @param sts subtableschema to add to 'this' database
     */
    public void addSubTableSchema(SubTableSchema sts) {
        String tableName = sts.getName();
        if (findSubTableSchema(tableName) != null) {
            throw new Error("multiple subtable declarations for " + tableName);
        }
        subtables.add(sts);
    }

    /**
     * return the list of subtable names of table with name tableName
     *
     * @param tableName
     * @return list of subtable names of tableName
     */
    public List<String> subtablesOf(String tableName) {
        TableSchema ts = this.findTableSchema(tableName);
        return subtablesOf(ts);
    }

    /**
     * return the list of subtable names of table whose schema is tschema
     *
     * @param tschema of parent table
     * @return list of subtable names of tschema
     */
    public List<String> subtablesOf(TableSchema tschema) {
        // Step 1: initialize list with root of inheritance hierarchy
        LinkedList<String> tlist = new LinkedList<>();
        tlist.add(tschema.getName());

        // Step 2: find a subtable schema for this table schema
        //         if there is none, we're done
        SubTableSchema sts = findSubTableSchema(tschema.getName());
        if (sts == null) {
            return tlist;
        }

        // Step 3: add the list of subtable names, recursively
        for (TableSchema ts : sts.getSubTableSchemas()) {
            tlist.addAll(subtablesOf(ts));
        }

        return tlist;
    }

    /**
     * return the name of the schema
     *
     * @return the name of the schema
     */
    public String getName() {
        return name;
    }

    /**
     * full (file) name of a schema is (name).schema.pl
     *
     * @return manufactured file name of schema
     */
    public String getFullName() {
        return name + ".schema.pl";
    }

    /**
     * return list of TableSchemas of this database schema
     *
     * @return list of TableSchemas of this database schema
     */
    public LinkedList<TableSchema> getTableSchemas() {
        return schemas;
    }

    /**
     * return list of SubTableSchemas of this database schema
     *
     * @return list of SubTableSchemas of this database schema
     */
    public LinkedList<SubTableSchema> getSubTableSchemas() {
        return subtables;
    }

    /**
     * return number of TableSchemas (equivalently the number of Tables in a
     * schema instance) in this dbschema
     *
     * @return number of TableSchemas in this db schema
     */
    public int size() {
        return schemas.size();
    }

    /**
     *
     * @return true if the schema has been flattened
     */
    public boolean isFlattened() {
        return flattened;
    }

    void setFlattened() {
        flattened = true;
    }

    /**
     * return the TableSchema with name tableName
     *
     * @param tableName of schema to return
     * @return the TableSchema with name tableName, null if not found
     */
    public TableSchema findTableSchema(String tableName) {
        for (TableSchema ts : schemas) {
            if (ts.getName().equals(tableName)) {
                return ts;
            }
        }
        return null;
    }

    /**
     * return the TableSchema with name tableName
     *
     * @param tableName of schema to return
     * @return the TableSchema with name tableName, throw Error if not foundd
     */
    public TableSchema findTableSchemaEH(String tableName) { // built-in error handling
        TableSchema ts = findTableSchema(tableName);
        if (ts == null) {
            throw new Error("no declaration for table " + tableName);
        }
        return ts;
    }

    /**
     * return the SubTableSchema for table with name tableName
     *
     * @param tableName of target subtableschema
     * @return the subTableSchema for table with name tableName, null if not
     * found
     */
    public SubTableSchema findSubTableSchema(String tableName) {
        for (SubTableSchema s : subtables) {
            if (s.getName().equals(tableName)) {
                return s;
            }
        }
        return null;
    }

    /**
     * rename DBschema
     *
     * @param name new DBSchema name
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * propagate attributes of superTables to subTables (operation is performed
     * on schema defs)
     */
    public void flatten() {
        // Step 1: must order subtable list by dominance (parent dominates a child)
        LinkedList<SubTableSchema> list = subtables;
        subtables = new LinkedList<>();
        while (!list.isEmpty()) {
            SubTableSchema next = dominant(list);
            list.remove(next);
            subtables.add(next);
        }

        // Step 2: propagate parent attributes to subattributes
        for (SubTableSchema st : subtables) {
            st.flatten();
        }
        flattened = true;
    }

    private SubTableSchema dominant(LinkedList<SubTableSchema> list) {
        boolean foundit = true;
        SubTableSchema last = null;

        for (SubTableSchema outer : list) {
            last = outer;
            for (SubTableSchema inner : list) {
                if (inner.contains(outer.getSuper())) {
                    foundit = false;
                    break;
                }
            }
            if (foundit) {
                return outer;
            }
            foundit = true;
        }
        throw new Error("table " + last.getName() + " participates in inheritance cycle");
    }

    String printHeader() {
        String db = "dbase(" + name + ",[";
        String comma = "";
        for (TableSchema ts : schemas) {
            db = db + comma + ts.getName();
            comma = ",";
        }
        return db + "]).\n";
    }

    /**
     * print the database schema definition to PrintStream out
     *
     * @param out PrintStream destination
     */
    public void print(PrintStream out) {
        out.format("%s\n", printHeader());

        for (TableSchema ts : schemas) {
            ts.print(out);
        }

        if (!subtables.isEmpty()) {
            out.format("\n");
            for (SubTableSchema st : subtables) {
                st.print(out);
            }
        }
    }

//    public void printFullSchema(PrintStream out) {
//        print(out);
//        out.format("\n");
//        printTupleDefs(out);
//        printInheritanceRules(out);
//    }
//
//    private void printTupleDefs(PrintStream out) {
//        for (TableSchema ts : schemas) {
//            ts.writeTupleDef(out);
//        }
//    }
//
//    private void printInheritanceRules(PrintStream out) {
//        out.format("\n");
//        for (TableSchema ts : schemas) {
//            ts.printInheritanceRule(out);
//            SubTableSchema sts = findSubTableSchema(ts.getName());
//            if (sts != null) {
//                sts.printInheritanceRules(out);
//            }
//        }
//    }
}
