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
public class SchemaCopyTest extends CommonTest {

    public SchemaCopyTest() {
    }

    @Test
    public void test1() {
        genericCopySchemaTest("violetdb.schema.pl");
    }

   @Test
    public void test2() {
        genericCopySchemaTest("inh.schema.pl");
    }
    
    @Test
    public void test3() {
        genericCopySchemaTest("complex.schema.pl");
    }
    
    @Test
    public void test4() {
        genericCopySchemaTest("hierarchy.schema.pl");
    }
}
