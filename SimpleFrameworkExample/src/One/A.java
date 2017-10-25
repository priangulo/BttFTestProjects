/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package One;

/**
 *
 * @author don
 */
public class A {
    int a;
    public A(String x) { a = Integer.parseInt(x); }
    public void inc() { a++; }
    public String toString() { return a+""; }
    public void add2() { inc(); inc(); }
}
