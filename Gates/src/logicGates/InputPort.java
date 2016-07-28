/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

import Errors.NoValueSet;
import Pins.*;
import GatesApp.*;
import java.util.*;

/**
 *
 * @author don
 */
public class InputPort extends Gate {

    public InputPort(String name) {
        super(name);
        OutputPin o = new OutputPin("o",this);
        outputs.put("o", o);
        if (Feature.tables) {
            table.add(this);
        }
    }
    
    public OutputPin getOutput() {
        return outputs.get("o");
    }
    
    @Feature(Feature.tables)     
    static LinkedList<Gate> table;
    
    public static void resetTable() {
        table = new LinkedList<>();
    }
    
    public static LinkedList<Gate> getTable() { 
        return table;
    }
    
    @Feature(Feature.eval)   /* for evaluation */            
    Value value = Value.UNKNOWN;
        
    public void setValue(Value v) {
        value = v;
    }
    
    public Value getValue() {
        if (value == Value.UNKNOWN)
           throw new NoValueSet("for port " + name);
        return value;
    }
}
