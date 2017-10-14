/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Applications;

/**
 *
 * @author don
 */
public class fmwk {
    
    static framework.Factory f;

    public static void main(String... args) {
        f = new pluginOne.Factory();
        framework.A a1 = f.NewA("4");
        doit();
        System.out.println("---------------------");
        f = new pluginTwo.Factory();
        doit();
    }
    
    static void doit() {
        framework.A a1 = f.NewA("4");
        a1.inc();
        a1.add2();
        System.out.println(a1);
        framework.B b1 = f.NewB();
        b1.inc();
        b1.add2();
        System.out.println(b1);
    }
}
