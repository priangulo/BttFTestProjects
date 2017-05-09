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
public class SchemaErrorsTest extends CommonTest {

    public SchemaErrorsTest() {
    }

    @Test
    public void test1() {
        genericError("complex.schema.pl");
    }
    
    @Test
    public void test2() {
        genericError("violetdb.schema.pl");
    }

    
    @Test
    public void test3() {
        genericError("inh.schema.pl");
    }
    
    @Test
    public void test4() {
        genericError("hierarchy.schema.pl");
    }
    
    @Test
    public void test5() {
        genericError("hierarchy2.schema.pl");
    }
    
    @Test
    public void test6() {
        genericError("complex2.schema.pl");
    }
    
    @Test
    public void test7() {
        genericError("complex3.schema.pl");
    }
}
