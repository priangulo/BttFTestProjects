package GatesApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import logicGates.Wire;
import logicGates.*;

/**
 *
 * @author don
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        aCircuit();
        System.out.println("Done!");
    }
    
    public static void aCircuit() {
        // is a == b?
        if (Feature.tables) {
            Gate.resetDB();
        }
        
        InputPort a = new InputPort("a");
        InputPort b = new InputPort("b");
        OutputPort r = new OutputPort("r");
        
        Not n1 = new Not("n1");
        Not n2 = new Not("n2");
        
        And a1 = new And("a1");
        And a2 = new And("a2");
        
        Or o1 = new Or("o1");
        
        new Wire(a,n1,"i1");
        new Wire(n1,a1,"i1");
        new Wire(b,a1,"i2");
        
        new Wire(a,a2,"i1");
        new Wire(b,n2,"i1");
        new Wire(n2,a2,"i2");
        
        new Wire(a1,o1,"i1");
        new Wire(a2,o1,"i2");
        new Wire(o1,r);
                
        if (Feature.tables) {
            Gate.printDB();
        }
        
        if (Feature.constraints) {
            boolean result = Gate.verify();
            System.out.println("Model is correct: " + result);
            if (!result)
                return;
        }
        
        if (Feature.eval) {
            
            a.setValue(Value.TRUE);
            b.setValue(Value.FALSE);


            Value rvalue = r.getValue();
            if (rvalue != Value.TRUE) {
                System.out.println("r value is wrong");
            } else {
                System.out.println("\nEvaluation of circuit is Correct!");
            }
        }
    }
}
