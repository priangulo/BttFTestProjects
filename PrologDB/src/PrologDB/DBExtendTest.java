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
public class DBExtendTest extends CommonTest {

    public DBExtendTest() {
    }

    @Test
    public void testNoLinks() {
        genericExtend("noLinks.violetdb.pl");
    }
    
    @Test
    public void testSchool() {
        genericExtend("school.violetdb.pl");
    }
    
    @Test
    public void testviolet() {
        genericExtend("violet.violetdb.pl");
    }
    
    @Test
    public void testwlinks() {
        genericExtend("wlinks.violetdb.pl");
    }
}
