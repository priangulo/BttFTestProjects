package com.example.expressiontree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @class SymbolTable
 * 
 * @brief This class stores variables and their values for use by
 *        the Interpreter.  It plays the role of the "Context" in
 *        the Interpreter pattern.
 */
public class SymbolTable {
    /** Hash table containing variable names and values. */
    private HashMap<String, Integer> map =
        new HashMap<String, Integer>();

    /** Ctor */
    public SymbolTable() {
    }

    public int get(String variable) {
        /** If variable isn't set then assign it a 0 value. */
        if(map.get(variable) != null)
            return map.get(variable);
        else {
            map.put(variable, 0);
            return map.get(variable);
        }
    }

    /** Set the value of a variable. */
    public void set(String variable, int value) {
        map.put(variable, value);
    }

    /** 
     * Print all variables and their values as an aid for
     * debugging.
     */
    public void print() {
        for (Iterator<Entry<String, Integer>> it =
                 map.entrySet().iterator();
             it.hasNext();
             ) {
            Entry<String,Integer> x = it.next();
            Platform.instance().outputLine((x.getKey()
                                            + " = "
                                            + x.getValue()));
        }
    }

    /** Clear all variables and their values. */
    public void reset() {
        map.clear();
    }
}