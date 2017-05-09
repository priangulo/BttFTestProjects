/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologScanner;

import org.junit.Test;

/**
 *
 * @author don
 */
public class dbaseTest {

    public dbaseTest() {
    }

    public void doit(String input, String outFile) {
        String outputFile = "TestData/" + outFile + ".txt";
        String correctFile = "CorrectData/Parser/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(outputFile);
        dScan ds = new dScan(input,-1);
        System.out.println(input);
        try {
            ds.parser(dScan.dbaseStmt);
            System.out.println("parse is successful!");
            for (String s : ds.parseList) {
                System.out.print(s + " ");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        RegTest.Utility.validate(outputFile, correctFile, false);
    }

    @Test
    public void test1() {
        doit("dbase( abc, [ id,abc,def ] ).", "db1");
    }

    @Test
    public void test2() {
        doit("dbase(abc,[id]).", "db2");
    }

    @Test
    public void test3() {
        doit("dbase (   a,  [id] ).", "db3");
    }

    // Error testing
    @Test
    public void test4() {
        doit("dbas ((abc,[id]).", "db4");
    }

    @Test
    public void test5() {
        doit("dbase((abc,[id]).", "db5");
    }
    
    @Test
    public void test6() {
        doit("dbase(abc,[]).", "db6");
    }
    
    @Test
    public void test7() {
        doit("dbase(abc[id]).", "db7");
    }
    
    @Test
    public void test8() {
        doit("dbase(abc,[id,'abc'])", "db8");
    }
    
    @Test
    public void test9() {
        doit("dbase(abc,[id,])", "db9");
    }
}
