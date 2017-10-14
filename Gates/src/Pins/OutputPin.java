/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pins;

//import java.util.*;
//import logicGates.*;

import java.util.AbstractList;
import java.util.LinkedList;

import GatesApp.Value;
//import GatesApp.*;
import logicGates.Gate;
import logicGates.Wire;

/**
 *
 * @author don
 */
public class OutputPin {
	int value;
    public String name;
    Gate outputOf;
    AbstractList<Wire> wiresFrom;
    
    public OutputPin(String name, Gate parent) {
        this.name = name;
        outputOf = parent;
        wiresFrom = new LinkedList<Wire>();
        value = Value.UNKNOWN;
    }
    
    public void addWire(Wire w) {
        wiresFrom.add(w);
    }
    
    public String toString() {
        return outputOf.name + "." +name;
    }
    
    public String nameOfGate() {
        return outputOf.name;
    }
    
    //@Feature(Feature.constraints)    
    public boolean isUsed() {
        return !wiresFrom.isEmpty();
    }
    
    //@Feature(Feature.eval)    
    public int getValue() {
        return outputOf.getValue();
    }
}
