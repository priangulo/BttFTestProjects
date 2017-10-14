/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

/**
 *
 * @author don
 */
public abstract class B {
    A[] ar = {NewA("1"),NewA("2"),NewA("3")};
    public void inc() {
        for (int i=0;i<3; i++)
            ar[i].inc();
    }
    public void add2() {
        for (int i=0;i<3; i++)
            ar[i].add2();
    }
    public String toString() {
        String result = "";
        for (int i=0; i<3; i++)
            result = result + ar[i].toString() + " ";
        return result;
    }
    public abstract A NewA(String x);
}
