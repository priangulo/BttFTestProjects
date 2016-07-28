/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

import Pins.*;
import GatesApp.*;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author don
 */
public class Wire extends Printable {
    public InputPin i;
    public OutputPin o;
    
    public Wire( OutputPin o, InputPin i ) {
        this.i = i;
        this.o = o;
        i.addWire(this);
        o.addWire(this);
        if (Feature.tables) {
            table.add(this);
        }
    }
    
    public Wire( InputPort o, Gate i, String name) {
        this(o.getOutput(), i.getInput(name));
    }
    
    public Wire( Gate from, String frompin, Gate to, String topin ) {
        this(from.getOutput(frompin),to.getInput(topin));
    }
    
    public Wire( Gate from, Gate to ,String topin ) {
        this(from, "o", to, topin);
    }
    
    public Wire( Gate from, OutputPort port) {
        this(from.getOutput("o"),port.getInput());
    }
    
    public void print(String x) {
        System.out.println("wire from " + o + " to " + i);   // param x is ignored
    }
    
    @Feature(Feature.tables)    
    static LinkedList<Wire> table;
    
    public static void resetTable() {
        table = new LinkedList<>();
    }
    
    public static LinkedList<Wire> getTable() { 
        return table;
    }
    
    @Feature(Feature.constraints)    
    public boolean isUsed() {
        return i.isUsed() && o.isUsed();
    }
    
    public static boolean verify() {
        boolean OK = true;
        for (Wire w : table) {
            OK = w.isUsed() && OK;
        }
        return OK;
    }
    
    @Feature(Feature.eval)    
    public Value getValue() {
        return o.getValue();
    }
}
