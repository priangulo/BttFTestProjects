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
public class tupleTest {

    public tupleTest() {
    }

    public void doit(String input, String outFile) {
        String outputFile = "TestData/" + outFile + ".txt";
        String correctFile = "CorrectData/Parser/" + outFile + ".txt";
        RegTest.Utility.redirectStdOut(outputFile);
        dScan ds = new dScan(input,-1);
        System.out.println(input);
        try {
            ds.parser(dScan.tupleStmt);
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
        doit("abc( a,b,c,d ).", "tuple1");
    }

    @Test
    public void test2() {
        doit("abc( a ).", "tuple2");
    }

    @Test
    public void test3() {
        doit("abc(a,b,c,d).", "tuple3");
    }
     @Test
    public void test31() {
        doit("abc(1,2,3).", "tuple31");
    }
    
    @Test
    public void test32() {
        doit("abc( 1, 2, 3).", "tuple32");
    }

    @Test
    public void test33() {
        doit("abc(1).", "tuple33");
   }
    
     @Test
    public void test41() {
        doit("abc('1','n2','3').", "tuple41");
    }
    
    @Test
    public void test42() {
        doit("abc  ('1',   'n2',   '3'  )   .", "tuple42");
    }

    @Test
    public void test43() {
        doit("abc('1').", "tuple43");
    }
    
    @Test
    public void test44() {
        doit("abc('1',a,3,45,b, 'cde,fghi').", "tuple44");
    }
    
    @Test
    public void test50() {
        doit("abc(1.3).", "tuple50");
    }
    
    @Test
    public void test51() {
        doit("abc(1.3,1,1.334,1444.0000).", "tuple51");
    }
    
    // Error testing
    @Test
    public void test4() {
        doit("abc ( a,b,c,d)..", "tuple4");
    }

    @Test
    public void test5() {
        doit("dbase((abc,[id]).", "tuple5");
    }
    
    @Test
    public void test6() {
        doit("dbase(abc,[id]).", "tuple6");
    }
    @Test
    public void test7() {
        doit("abc( ).", "tuple332");
    }
    
    @Test
    public void test751() {
        doit("abc(1.3.1,1.334,1444.0000).", "tuple751");
    }

}
