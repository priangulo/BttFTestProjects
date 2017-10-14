/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginOne;

/**
 *
 * @author don
 */
public class A extends framework.A {
    int a;
    A(String x) { a = Integer.parseInt(x); }
    public void inc() { a++; }
    public String toString() { return a+""; }
}
