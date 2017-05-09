/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologDB;

import org.junit.Test;

/**
 *
 * @author don
 */
public class DBCopyTest extends CommonTest {

    public DBCopyTest() {
    }

    @Test
    public void test1() {
        genericCopyDBTest("noLinks.violetdb.pl");
    }

    @Test
    public void test2() {
        genericCopyDBTest("school.violetdb.pl");
    }
    
    @Test
    public void test3() {
        genericCopyDBTest("violet.violetdb.pl");
    }
    
    @Test
    public void test4() {
        genericCopyDBTest("wLinks.violetdb.pl");
    }
    
   @Test
    public void test5() {
        genericCopyDBTest("i.inh.pl");
    }
    
    @Test
    public void test6() {
        genericCopyDBTest("c.complex.pl");
    }
    
    @Test
    public void test7() {
        genericCopyDBTest("h.hierarchy.pl");
    }
    
}
