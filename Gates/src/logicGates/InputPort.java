/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

import java.util.LinkedList;

import Errors.NoValueSet;
import GatesApp.Value;
//import Pins.*;
//import GatesApp.*;
import Pins.OutputPin;

//import java.util.*;

/**
 *
 * @author don
 */
public class InputPort extends Gate {

    public InputPort(String name) {
        super(name);
        OutputPin o = new OutputPin("o",this);
        outputs.put("o", o);
        //if (Feature.tables) {
            table.add(this);
        //}
    }
    
    public OutputPin getOutput() {
        return outputs.get("o");
    }
    
    //@Feature(Feature.tables)     
    static LinkedList<Gate> table;
    
    public static void resetTable() {
        table = new LinkedList<Gate>();
    }
    
    public static LinkedList<Gate> getTable() { 
        return table;
    }
    
    //@Feature(Feature.eval)   /* for evaluation */            
    int value = Value.UNKNOWN;
        
    public void setValue(int v) {
        value = v;
    }
    
    public int getValue() {
        if (value == Value.UNKNOWN)
           throw new NoValueSet("for port " + name);
        return value;
    }
}
