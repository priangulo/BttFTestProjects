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
public class Orig {

    public static void main(String... args) {
        One.A a1 = new One.A("4");
        a1.inc();
        a1.add2();
        System.out.println(a1);
        One.B b1 = new One.B();
        b1.inc();
        b1.add2();
        System.out.println(b1);
        System.out.println("---------------------");
        Two.A a2 = new Two.A("4");
        a2.inc();
        a2.add2();
        System.out.println(a2);
        Two.B b2 = new Two.B();
        b2.inc();
        b2.add2();
        System.out.println(b2);
    }

}
