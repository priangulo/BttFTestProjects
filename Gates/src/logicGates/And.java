/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

import Pins.*;
import GatesApp.*;
import java.util.*;

/**
 *
 * @author don
 */
public class And extends Gate {
    InputPin i1, i2;
    OutputPin o;

    public And(String name) {
        super(name);
        i1 = new InputPin("i1",this);
        i2 = new InputPin("i2",this);
        inputs.put("i1",i1);
        inputs.put("i2",i2);
        o = new OutputPin("o",this);
        outputs.put("o", o);
        
        if (Feature.tables) {
            table.add(this);
        }
    }
    
    @Feature(Feature.tables)     
    static LinkedList<Gate> table;
    
    public static void resetTable() {
        table = new LinkedList<>();
    }
    
    public static LinkedList<Gate> getTable() { 
        return table;
    }
    
        
    @Feature(Feature.eval)    /* for evaluation */    
    public Value getValue() { 
        Value v1 = i1.getValue();
        Value v2 = i2.getValue();
        if (v1==Value.TRUE && v2==Value.TRUE)
            return Value.TRUE;
        else
            return Value.FALSE;
    }
    
}
