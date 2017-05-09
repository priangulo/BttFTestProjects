/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologScanner;

import PrologScanner.dScan;
import PrologTokens.RightParen;
import PrologTokens.Comma;
import PrologTokens.End;
import PrologTokens.Name;
import PrologTokens.LeftParen;
import PrologTokens.ColumnDecls;
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
public class tableTest {

    public tableTest() {
    }

    Token[] tableStmt = {new Fixed("table"), new LeftParen(), new Name(), new Comma(), new LeftBracket(), new ColumnDecls(), new RightBracket(),
        new RightParen(), new Dot(), new End()};

    public void doit(String input, String outFile) {
        String outputFile = "TestData/" + outFile + ".txt";
        String correctFile = "CorrectData/Parser/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(outputFile);
        dScan ds = new dScan(input,-1);
        System.out.println(input);
        try {
            ds.parser(tableStmt);
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
        doit("table( abc, [ id,\"abc\",def ] ).", "test1");
    }

    @Test
    public void test2() {
        doit("table(abc,[id,\"abc\",def]).", "test2");
    }

    @Test
    public void test3() {
        doit("table (   a,  [id] ).", "test3");
    }

    @Test
    public void test4() {
        doit("dbas ((abc,[id]).", "test4");
    }

    @Test
    public void test5() {
        doit("table((abc,[id]).", "test5");
    }
    
    @Test
    public void test6() {
        doit("table(abc,[]).", "test6");
    }
    
    @Test
    public void test7() {
        doit("table(abc[id]).", "test7");
    }
    
    @Test
    public void test8() {
        doit("table(abc,[id])", "test8");
    }
}
