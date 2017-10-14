/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginTwo;

/**
 *
 * @author don
 */
public class A extends framework.A {
    double b;
    A(String x) { b = Double.parseDouble(x); }
    public void inc() { b++; }
    public String toString() { return b+""; }
}
