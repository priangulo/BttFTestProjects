/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

import Pins.*;
import GatesApp.*;
import java.util.LinkedList;

/**
 *
 * @author don
 */
public class Not extends Gate {
    InputPin i1;

    public Not(String name) {
        super(name);
        i1 = new InputPin("i1",this);
        inputs.put("i1",i1);
        OutputPin o = new OutputPin("o",this);
        outputs.put("o", o);
        if (Feature.tables) {
            table.add(this);
        }
    }
    
    @Feature(Feature.tables)     
    static LinkedList<Not> table;
    
    public static void resetTable() {
        table = new LinkedList<>();
    }
    
    public static LinkedList<Not> getTable() { 
        return table;
    }
    
    @Feature(Feature.eval)   /* for logic diagram evaluation */    
    public Value getValue() {
        Value v = i1.getValue();
        return (v == Value.TRUE)? Value.FALSE : Value.TRUE;
    }
    
}
