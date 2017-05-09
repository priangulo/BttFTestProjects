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
public class ComplexDBExtendTest extends CommonTest {

    public ComplexDBExtendTest() {
    }

    // complex because it has inheritance declaration
    
    @Test
    public void test1() {
        genericExtend("c.complex.pl");
    }
    
    @Test
    public void test2() {
        genericExtend("h.hierarchy.pl");
    }
    
    @Test
    public void test3() {
        genericExtend("i.inh.pl");
    }
}
