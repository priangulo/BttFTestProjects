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
public class Factory extends framework.Factory {

    public framework.A NewA(String x) {
        return new A(x);
    }

    public framework.B NewB() {
        return new B();
    }
}
