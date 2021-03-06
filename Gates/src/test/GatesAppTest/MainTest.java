package test.GatesAppTest;

import GatesApp.Value;
import logicGates.And;
import logicGates.Gate;
import logicGates.InputPort;
import logicGates.Not;
import logicGates.Or;
import logicGates.OutputPort;
import logicGates.Wire;
import org.junit.Test;

public class MainTest {

    public MainTest() {
    }
    
    @Test
    public void easy() {
        InputPort a, b;
        OutputPort r;
        And and;
        
        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut("output.txt");

        System.out.println("(a^b)=r;");
        
        Gate.resetDB();
        a = new InputPort("a");
        b = new InputPort("b");
        r = new OutputPort("r");
        and = new And("and1");
        
        new Wire(a, and, "i1");
        new Wire(b, and, "i2");
        new Wire(and, r);
        
        Gate.printDB();

        System.out.println("Model is correct: " + Gate.verify());

        boolean noErrors = true;

        a.setValue(Value.TRUE);
        b.setValue(Value.FALSE);
        int rvalue = r.getValue();
        if (rvalue != Value.FALSE) {
            System.out.println("r value is wrong");
            noErrors = false;
        }
        if (noErrors) {
            System.out.println("\nEvaluation of circuit is Correct!");
        }
        System.out.println("---------------------");

        RegTest.Utility.validate("output.txt", "Correct/easy.txt", false);
        
    }

    @Test
    public void h2Example() {
        InputPort a, b, c, d;
        OutputPort r, t;
        Or or1, or2, or3;
        And and1;

        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut("output.txt");

        System.out.println("(avb)^(cvd)=r; (avb)v(cvd) = t;");
        Gate.resetDB();

        a = new InputPort("a");
        b = new InputPort("b");
        c = new InputPort("c");
        d = new InputPort("d");
        r = new OutputPort("r");
        t = new OutputPort("t");

        or1 = new Or("or1");
        or2 = new Or("or2");
        or3 = new Or("or3");

        and1 = new And("and1");

        new Wire(a, or1, "i1");
        new Wire(b, or1, "i2");
        new Wire(c, or2, "i1");
        new Wire(d, or2, "i2");
        new Wire(or1, and1, "i1");
        new Wire(or2, and1, "i2");
        new Wire(or1, or3, "i1");
        new Wire(or2, or3, "i2");
        new Wire(and1, r);
        new Wire(or3, t);

        Gate.printDB();

        System.out.println("Model is correct: " + Gate.verify());

        boolean noErrors = true;

        a.setValue(Value.TRUE);
        b.setValue(Value.FALSE);
        c.setValue(Value.FALSE);
        d.setValue(Value.FALSE);
        int rvalue = r.getValue();
        int tvalue = t.getValue();
        if (rvalue != Value.FALSE) {
            System.out.println("r value is wrong");
            noErrors = false;
        }
        if (tvalue != Value.TRUE) {
            System.out.println("t value is wrong");
            noErrors = false;
        }
        if (noErrors) {
            System.out.println("\nEvaluation of circuit is Correct!");
        }

        System.out.println("---------------------");

        RegTest.Utility.validate("output.txt", "Correct/h2Example.txt", false);
    }

    @Test
    public void aEQb() {
        // is a == b?
        InputPort a, b;
        OutputPort r;
        And a1, a2;
        Or o1;
        Not n1, n2;

        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut("output.txt");
        
        System.out.println("a == b");
        
        Gate.resetDB();

        a = new InputPort("a");
        b = new InputPort("b");
        r = new OutputPort("r");
        a1 = new And("a1");
        a2 = new And("a2");
        o1 = new Or("o1");
        n1 = new Not("n1");
        n2 = new Not("n2");

        new Wire(a, a1, "i1");
        new Wire(b, a1, "i2");
        new Wire(a, n1, "i1");
        new Wire(b, n2, "i1");
        new Wire(n1, a2, "i1");
        new Wire(n2, a2, "i2");
        new Wire(a1, o1, "i1");
        new Wire(a2, o1, "i2");
        new Wire(o1, r);

        Gate.printDB();

        boolean result = Gate.verify();
        System.out.println("Model is correct: " + result);
        if (!result) {
            return;
        }

        a.setValue(Value.TRUE);
        b.setValue(Value.FALSE);

        int rvalue = r.getValue();
        if (rvalue != Value.FALSE) {
            System.out.println("r value is wrong");
        } else {
            System.out.println("\nEvaluation of circuit is Correct!");
        }

        System.out.println("---------------------");
        RegTest.Utility.validate("output.txt", "Correct/aEQb.txt", false);
    }

    @Test
    public void aNEb() {
        // is a != b?

        RegTest.Utility.init();
        RegTest.Utility.redirectStdOut("output.txt");
        
        System.out.println("a != b");

        Gate.resetDB();

        InputPort a = new InputPort("a");
        InputPort b = new InputPort("b");
        OutputPort r = new OutputPort("r");

        Not n1 = new Not("n1");
        Not n2 = new Not("n2");

        And a1 = new And("a1");
        And a2 = new And("a2");

        Or o1 = new Or("o1");

        new Wire(a, n1, "i1");
        new Wire(n1, a1, "i1");
        new Wire(b, a1, "i2");

        new Wire(a, a2, "i1");
        new Wire(b, n2, "i1");
        new Wire(n2, a2, "i2");

        new Wire(a1, o1, "i1");
        new Wire(a2, o1, "i2");
        new Wire(o1, r);

        Gate.printDB();

        boolean result = Gate.verify();
        System.out.println("Model is correct: " + result);
        if (!result) {
            return;
        }

        a.setValue(Value.TRUE);
        b.setValue(Value.FALSE);

        int rvalue = r.getValue();
        if (rvalue != Value.TRUE) {
            System.out.println("r value is wrong");
        } else {
            System.out.println("\nEvaluation of circuit is Correct!");
        }

        System.out.println("---------------------");
        RegTest.Utility.validate("output.txt", "Correct/aNEb.txt", false);
    }
}
