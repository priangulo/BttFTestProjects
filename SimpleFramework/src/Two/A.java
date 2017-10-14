/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Two;

/**
 *
 * @author don
 */
public class A {
    double a;
    public A(String x) { a = Double.parseDouble(x); }
    public void inc() { a++; }
    public String toString() { return a+""; }
    public void add2() { inc(); inc(); }
}
