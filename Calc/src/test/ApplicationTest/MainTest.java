/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.ApplicationTest;

import Application.Main;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author don
 */
public class MainTest {
    
    public MainTest() {
    }

    @Test
    public void t1() {
        Main m = new Main();
        m.g.ResultSetText("4");
        m.g.EnterActionPerformed(null);
        m.g.ResultSetText("7");
        m.g.TimesActionPerformed(null);
        String x = m.g.ResultGetText();
        assert(x.equals("28")||x.equals("28.0"));
    }
    
    @Test
    public void t2() {
        Main m = new Main();
        m.g.ResultSetText("4");
        m.g.EnterActionPerformed(null);
        m.g.ResultSetText("5");
        m.g.PlusActionPerformed(null);
        m.g.ResultSetText("10");
        m.g.MinusActionPerformed(null);
        String x = m.g.ResultGetText();
        assert(x.equals("-1")||x.equals("-1.0"));
    }
    
    @Test
    public void t3() {
        Main m = new Main();
        m.g.ResultSetText("40");
        m.g.EnterActionPerformed(null);
        m.g.ResultSetText("8");
        m.g.DivActionPerformed(null);
        m.g.ResultSetText("7");
        m.g.PlusActionPerformed(null);
        String x = m.g.ResultGetText();
        assert(x.equals("12")||x.equals("12.0"));
    }
    
    @Test
    public void t4() {
        Main m = new Main();
        m.g.ResultSetText("8");
        m.g.EnterActionPerformed(null);
        m.g.ResultSetText("2");
        m.g.TimesActionPerformed(null);
        m.g.ResultSetText("16");
        m.g.DivActionPerformed(null);
        String x = m.g.ResultGetText();
        assert(x.equals("1")||x.equals("1.0"));
    }
    
}
