/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicGates;

//import Pins.*;
//import GatesApp.*;
import Pins.InputPin;

import java.util.LinkedList;

//import java.util.*;
//import static logicGates.InputPort.table;

import GatesApp.Value;

/**
 *
 * @author don
 */
public class OutputPort extends Gate {

    public OutputPort(String name) {
        super(name);
        InputPin i1 = new InputPin("i1",this);
        inputs.put("i1",i1);
        //if (Feature.tables) {
            table.add(this);
        //}
    }
    
    public InputPin getInput() { 
        return inputs.get("i1");
    }
    
    //@Feature(Feature.tables)     
    static LinkedList<Gate> table;
    
    public static void resetTable() {
        table = new LinkedList<Gate>();
    }
    
    public static LinkedList<Gate> getTable() { 
        return table;
    }
    
    //@Feature(Feature.eval)    
    public int getValue() {
        return getInput().getValue();
    }
}
