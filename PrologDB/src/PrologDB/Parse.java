package PrologDB;

import PrologScanner.dScan;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parse {

    /**
     * turn Debug on if you want to see stack trace on Errors
     */
    static boolean Debug = false;

    /**
     * a dbase declaration is on a single line. this method parses the given
     * line @ lineno returning the DBSchema given declared name, also returns a
     * list of tableNames (not their schemas)
     *
     * @param line
     * @param lineno
     * @param tableNames
     * @return DBSchema object with name of schema initialized; also returns
     * list of tableNames -- to be used later in parsing
     * @throws Error
     */
    public static DBSchema parseDBaseDecl(String line, int lineno, List<String> tableNames) throws Error {
        // Step 1: parse the line
        dScan ds = new dScan(line, lineno);
        ds.parser(dScan.dbaseStmt);

        // Step 2: unpack parsing
        LinkedList<String> list = ds.parseList;
        String schemaName = list.removeFirst();  // is name of the dbase schema
        DBSchema dbs = new DBSchema(schemaName);
        tableNames.addAll(list);
        return dbs;
    }

    /**
     * a subtable decl is on a single line. this method parses the given line @
     * lineno and adds the subtable declaratino to DBSchema dbs. an Error is
     * thrown if something wrong (parse or semantic error) is discovered.
     *
     * @param line to parse
     * @param lineno of line
     * @param dbs to add subtable definition
     */
    public static void parseSubTableDecl(String line, int lineno, DBSchema dbs) {
        if (dbs == null) {
            throw new Error("subtable defined before dbase");
        }

        // Step 1: parse the line
        dScan ds = new dScan(line, lineno);
        ds.parser(dScan.subTableStmt);

        // Step 2: unpack parsing to get table name, and list of values, and create an empty tuple
        LinkedList<String> list = ds.parseList;
        String tableName = list.removeFirst();  // this is the super table name
        TableSchema suptab = dbs.findTableSchemaEH(tableName);
        ArrayList<TableSchema> subTableSchemas = new ArrayList<>();
        for (String name : list) {
            TableSchema ts = dbs.findTableSchemaEH(name);
            subTableSchemas.add(ts);
        }

        // Step 3: assemble the subtableschema declaaration
        SubTableSchema sts = new SubTableSchema(suptab, subTableSchemas);
        dbs.addSubTableSchema(sts);
    }

    /**
     * a table declaration is on a single line. this method parses the given
     * line @ lineno and adds the table definition to schema dbs
     *
     * @param line to parse
     * @param lineno of line
     * @param dbs to add table declaration
     * @throws Error
     */
    public static void parseTableDecl(String line, int lineno, DBSchema dbs) throws Error {
        // Step 0: simple error checking;
        if (dbs == null) {
            throw new Error("table defined before dbase");
        }

        // Step 1: parse line
        dScan ds = new dScan(line, lineno);
        ds.parser(dScan.tableStmt);

        // Step 2: unpack parsing to get table name, and list of values, and create an empty tuple
        LinkedList<String> list = ds.parseList;
        String tableName = list.removeFirst();  // this is the table name
        TableSchema tableDef = new TableSchema(tableName);

        // Step 3: assemble the column defs
        for (String name : list) {
            boolean isQuoted = name.startsWith("\"");
            Column newColumn = new Column(name.replace("\"", ""), isQuoted);
            tableDef.addColumn(newColumn);
        }

        // Step 4: add the table definition to the database schema
        dbs.addTableSchema(tableDef);
    }

}
