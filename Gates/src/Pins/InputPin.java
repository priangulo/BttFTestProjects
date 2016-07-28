/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pins;

import GatesApp.*;
import logicGates.Gate;
import Errors.*;
import logicGates.Wire;

/**
 *
 * @author don
 */
public class InputPin {

    public String name;
    Gate inputOf;
    Wire wireFrom; // only from one source
    
    public InputPin(String name, Gate parent) {
        this.name = name;
        inputOf = parent;
        wireFrom = null;
    }
    
    public void addWire(Wire w) {
        if (wireFrom != null)
            throw new PinAlreadySet("pin "+ name + " of gate " + inputOf.name + " with " + wireFrom.o.name);
        wireFrom = w;
    }
    
    public String toString() {
        return inputOf.name + "." +name;
    }
    
    @Feature(Feature.constraints)    
    public boolean isUsed() {
        return wireFrom != null;
    }
    
    public String nameOfGate() {
        return inputOf.name;
    }
    
    @Feature(Feature.eval)    /*  this is for circuit execution */    
    public Value getValue() {
        return wireFrom.getValue();
    }
}
