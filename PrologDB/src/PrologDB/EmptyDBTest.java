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
public class EmptyDBTest extends CommonTest {

    public EmptyDBTest() {
    }

    @Test
    public void test1() {
        genericSchema2EmptyDB("violetdb.schema.pl");
    }

   @Test
    public void test2() {
        genericSchema2EmptyDB("inh.schema.pl");
    }
    
    @Test
    public void test3() {
        genericSchema2EmptyDB("complex.schema.pl");
    }
    
    @Test
    public void test4() {
        genericSchema2EmptyDB("hierarchy.schema.pl");
    }
}
