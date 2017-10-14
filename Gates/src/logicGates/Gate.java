/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

//import Pins.*;
//import Errors.*;
//import GatesApp.*;
import Pins.InputPin;
import Pins.OutputPin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

//import java.util.*;
//import static logicGates.And.table;

import Errors.NoPinFound;
import GatesApp.Value;

/**
 *
 * @author don
 */
public abstract class Gate extends Printable {

    public String name;
    HashMap<String, InputPin> inputs;
    HashMap<String, OutputPin> outputs;

    public Gate(String name) {
        this.name = name;
        inputs = new HashMap<String, InputPin>();
        outputs = new HashMap<String, OutputPin>();
    }

    public InputPin getInput(String name) {
        InputPin i = inputs.get(name);
        if (i == null) {
            throw new NoPinFound("input to gate " + this.name + "." + name + " not found");
        }
        return i;
    }

    public OutputPin getOutput(String name) {
        OutputPin o = outputs.get(name);
        if (o == null) {
            throw new NoPinFound("output to gate " + this.name + "." + name + " not found");
        }
        return o;
    }

    public void print(String gateType) {
        System.out.printf("%6s gate %10s with inputs ( ", gateType, name);
        String comma = "";
        for (InputPin i : inputs.values()) {
            System.out.print(comma + i);
            comma = ", ";
        }
        System.out.print(" ) and outputs ( ");
        comma = "";
        for (OutputPin o : outputs.values()) {
            System.out.print(comma + o);
            comma = ", ";
        }
        System.out.println(" )");
    }

    //@Feature(Feature.tables)
    public static void resetDB() {
        And.resetTable();
        Not.resetTable();
        Or.resetTable();
        Wire.resetTable();
        InputPort.resetTable();
        OutputPort.resetTable();
    }

    public static void printDB() {
        logicGates.Gate.printTable("And", And.getTable());
        logicGates.Gate.printTable("Or", Or.getTable());
        logicGates.Gate.printTable("Not", Not.getTable());
        logicGates.Gate.printTable("Wire", Wire.getTable());
        logicGates.Gate.printTable("InputPort", InputPort.getTable());
        logicGates.Gate.printTable("OutputPort", OutputPort.getTable());
    }

    public static <G extends Printable> void printTable(String ttype, LinkedList<G> t) {
        for (G g : t) {
            g.print(ttype);
        }
    }

    //@Feature(Feature.constraints)
    public boolean extra() {  // subclasses override this method if something special needs to be done
        return true;
    }

    public boolean allInputsUsed() {
        boolean OK = true;
        for (String i : inputs.keySet()) {
            InputPin p = inputs.get(i);
            if (!p.isUsed()) {
                System.out.println("input " + p.name + " of gate " + p.nameOfGate() + " is unused");
                OK = false;
            }
        }
        return OK;
    }

    public boolean allOutputsUsed() {
        boolean OK = true;
        for (String i : outputs.keySet()) {
            OutputPin p = outputs.get(i);
            if (!p.isUsed()) {
                System.out.println("output " + p.name + " of gate " + p.nameOfGate() + " is unused");
                OK = false;
            }
        }
        return OK;
    }

    public static <G extends Gate> boolean verify(String label, LinkedList<G> table) {
        HashSet<String> hs = new HashSet<String>();
        boolean OK = true;
        for (G a : table) {

            if (hs.contains(a.name)) {
                System.out.println("multiple " + label + " with the same name: " + a.name);
                OK = false;
            } else {
                hs.add(a.name);
            }

            OK = a.allInputsUsed() && OK;
            OK = a.allOutputsUsed() && OK;
            OK = a.extra() && OK;
        }
        return OK;
    }

    public static boolean verify() {
        boolean OK = true;
        OK = And.verify("And gate", And.getTable()) && OK;
        OK = Not.verify("Not gate", Not.getTable()) && OK;
        OK = Or.verify("Or gate", Or.getTable()) && OK;
        OK = InputPort.verify("InputPort", InputPort.getTable()) && OK;
        OK = Wire.verify() && OK;
        return OK;
    }

    //@Feature(Feature.eval)    
    public abstract int getValue();  // evaluate gate(inputs)
}
