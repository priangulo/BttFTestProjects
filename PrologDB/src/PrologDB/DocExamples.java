/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologDB;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author don
 */
public class DocExamples extends CommonTest {

    public DocExamples() {
    }

//    @Test
//    public void testprintSchema() {
//        System.out.format("\n\n START print Schema n\n");
//        try {
//            File sFile = new File("TestData/Schema/starTrek.schema.pl");
//            DBSchema s = DBSchema.readSchema(sFile, System.err);
//            // easy way
//            s.print(System.out);
//
//            // more detailed way
//            System.out.format("\n\n=========\n\n", null);
//            System.out.format("database %s has \n", s.getName());
//            for (TableSchema t : s.getTableSchemas()) {
//                System.out.format("   table %s with columns ", t.getName());
//                for (Column c : t.getColumns()) {
//                    char quote = c.isQuoted() ? '\'' : ' ';
//                    System.out.format("%c%s%c ", quote, c.getName(), quote);
//                }
//                System.out.format("\n");
//            }
//            System.out.format("\n");
//            for (SubTableSchema st : s.getSubTableSchemas()) {
//                TableSchema supr = st.getSuper();
//                System.out.format("   table %s has subtables ", supr.getName());
//                for (TableSchema chld : st.getSubTableSchemas()) {
//                    System.out.format("%s ", chld.getName());
//                }
//                System.out.format("\n");
//            }
//            System.out.format("\n\n");
//
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        System.out.format("\n\n END print Schema n\n");
//    }

//    DBSchema schemaBuild() {
//        try {
//            DBSchema trekSchema = new DBSchema("starTrek");
//
//            // create tables and add them to the schema
//            TableSchema crewman = new TableSchema("crewman");
//            {
//                crewman.addColumn(new Column("cid", false));  // false means non-quoted
//                crewman.addColumn(new Column("fname", false));
//                crewman.addColumn(new Column("lname", false));
//            }
//            TableSchema commander = new TableSchema("commander");
//            {
//                commander.addColumn(new Column("rank", false));
//            }
//            TableSchema lieutenant = new TableSchema("lieutenant");
//            {
//                lieutenant.addColumn(new Column("specialty", false));
//            }
//            trekSchema.addTableSchema(crewman);
//            trekSchema.addTableSchema(commander);
//            trekSchema.addTableSchema(lieutenant);
//
//            // now createe the lone subtable declaration, and add to schema
//            SubTableSchema sts = new SubTableSchema(crewman);
//            sts.addSubTableSchema(commander);
//            sts.addSubTableSchema(lieutenant);
//            trekSchema.addSubTableSchema(sts);
//
//            // now print the schema declaration
//            return trekSchema;
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return null;
//        }
//    }
    
//    @Test
//    public void testSchemaBuild() {
//        System.out.format("\n\n START schema build \n\n", null);
//        DBSchema trekSchema = schemaBuild();
//        trekSchema.print(System.out);
//        System.out.format("\n\n END schema build \n\n", null);
//    }

//    @Test
//    public void testDBread() {
//        System.out.format("\n\n START database read \n\n", null);
//        try {
//          File dbfile = new File("TestData/DB/enterprise.starTrek.pl");
//          DB db = DB.readDataBase(dbfile,System.err);
//          // easy way to print
//          db.print(System.out);
//          
//          // more detailed way
//          System.out.format("\n\n ===== \n\n",null);
//          System.out.format("database %s contains:\n", db.getName());
//          
//          for (Table t : db.getTables()){
//              System.out.format("Table %s\n",t.getName());
//              for (Tuple tup : t.getTuples()) {
//                  tup.print(System.out);
//              }
//              System.out.format("\n",null);
//          }
//          
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        System.out.format("\n\n END database read \n\n", null);
//    }

//    @Test 
//    public void testDBbuild() {
//        System.out.format("\n\n\n START build enterprise database\n\n",null);
//        DBSchema trekSchema = schemaBuild();
//        DB enterprise = new DB("enterprise",trekSchema);
//        
//        // create spock tuple
//        Table crewman = enterprise.findTableEH("crewman");
//        Tuple spock = new Tuple(crewman);
//        spock.addColumnValuesEH("c1", "mr","spock");
//        crewman.addTuple(spock);
//        
//        // create sulu tuple
//        Table lieutenant = enterprise.findTableEH("lieutenant");
//        Tuple sulu = new Tuple(lieutenant);
//        sulu.addColumnValuesEH("c3", "hikaru", "sulo", "navigation");
//        lieutenant.addTuple(sulu);
//        
//        // create kirk tupld
//        Table commander = enterprise.findTableEH("commander");
//        Tuple kirk = new Tuple(commander);
//        kirk.addColumnValuesEH("c2", "james", "kirk", "captain");
//        commander.addTuple(kirk);
//        
//        // now print database
//        enterprise.print(System.out);
//        System.out.format("\n\n END build enterprise database \n\n",null);
//    }
    
//    @Test
//    public void testTableRetrieve() {
//        System.out.format("BEGIN Table Retrival\n\n");
//        DB db = DB.readDataBase("TestData/DB/dogOwner.do.pl");
//        Table dog = db.findTable("dog");
//
//        // query 1: retrieve all aussies
//        List<Tuple> aussies = dog.find("breed", "aussie");
//        Tuple.print(aussies, "list of aussies", System.out);
//
//        // query 2: retrieve all aussies that are blacktri's
//        //          use prior list as starting point
//        List<Tuple> kaussies = dog.find(aussies, "color", "blacktri");
//        Tuple.print(kaussies, "list of black tri aussies", System.out);
//
//        System.out.format("END Table Retrival\n\n");
//    }
    
    @Test
    public void testDBTableRetrieve() {
        System.out.format("BEGIN DBTable Retrival\n\n");
        DB db = DB.readDataBase("TestData/DB/pets.petdb.pl");
        Table pet = db.findTableEH("pet");

        // query 1: retrieve all pets
        List<Tuple> allPets = db.getTuples(pet);
        Tuple.print(allPets, "list of all pets", System.out);

        // query 2: all pets whose name begins with "l"
        System.out.println("list of all pets whose names start with 'l'");
        for (Tuple t : allPets) {
            if (t.getValue("name").startsWith("l")) {
                t.print(System.out);
            }
        }
        
        System.out.format("END DBTable Retrival\n\n");
    }

//    @Test
//    public void testJoins() {
//        System.out.format("BEGIN joins\n\n");
//        DB db = DB.readDataBase("TestData/DB/dogOwner.do.pl");
//
//        // Step 1: join the dog and owner schemas
//        TableSchema dogSchema = db.getTableSchema("dog");
//        TableSchema ownerSchema = db.getTableSchema("owner");
//        TableSchema dXoSchema = dogSchema.join(ownerSchema);
//
//        // Step 2: get the tables to join
//        Table dog = db.findTable("dog");
//        Table when = db.findTable("when");
//        Table owner = db.findTable("owner");
//
//        // Step 3: join all 3 tables and produce dXoSchema tuples as output
//        LinkedList<Tuple> list = new LinkedList<>();
//        for (Tuple d : dog.getTuples()) {
//            for (Tuple w : when.find("did", d.getValue("did"))) {
//                for (Tuple o : owner.find("oid", w.getValue("oid"))) {
//                    Tuple t = new Tuple(dXoSchema).join(dXoSchema, d, o);
//                    list.add(t);
//                }
//            }
//        }
//        // Step 4: print list
//        dXoSchema.print(System.out);
//        Tuple.print(list, "joined dog and owner tuples", System.out);
//        System.out.format("END joins\n\n");
//    }

}
