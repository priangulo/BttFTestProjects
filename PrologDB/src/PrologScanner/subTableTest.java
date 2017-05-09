/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologScanner;

import PrologTokens.RightParen;
import PrologTokens.Comma;
import PrologTokens.End;
import PrologTokens.Name;
import PrologTokens.LeftParen;
import PrologTokens.NameList;
import PrologTokens.Dot;
import PrologTokens.RightBracket;
import PrologTokens.Fixed;
import PrologTokens.LeftBracket;
import PrologTokens.Token;
import org.junit.Test;

/**
 *
 * @author don
 */
public class subTableTest {

    public subTableTest() {
    }


    public void doit(String input, String outFile) {
        String outputFile = "TestData/" + outFile + ".txt";
        String correctFile = "CorrectData/Parser/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(outputFile);
        dScan ds = new dScan(input,-1);
        System.out.println(input);
        try {
            ds.parser(dScan.subTableStmt);
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
        doit("subtable( abc, [ id,abc,def ] ).", "st1");
    }

    @Test
    public void test2() {
        doit("subtable(abc,[id]).", "st2");
    }

    @Test
    public void test3() {
        doit("subtable (   a,  [id] ).", "st3");
    }

    // Error testing
    @Test
    public void test4() {
        doit("sub ((abc,[id]).", "st4");
    }

    @Test
    public void test5() {
        doit("subtable((abc,[id]).", "st5");
    }
    
    @Test
    public void test6() {
        doit("subtable(abc,[]).", "st6");
    }
    
    @Test
    public void test7() {
        doit("subtable(abc[id]).", "st7");
    }
    
    @Test
    public void test8() {
        doit("subtable(abc,[id,'abc'])", "st8");
    }
    
    @Test
    public void test9() {
        doit("subtable(abc,[id,])", "st9");
    }
}
