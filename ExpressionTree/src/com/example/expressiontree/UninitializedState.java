package com.example.expressiontree;

import java.util.HashMap;

/** 
 * A state without an initialized context or format.
 */
public class UninitializedState extends State {
    /** Formats the traversal order of the state. */
    public void format(TreeOps context, String newFormat) {
        if (newFormat.equals(""))
            /** 
             * Default to in-order if user doesn't explicitly
             * request a format order.
             */
            newFormat = "in-order";
        else if (!newFormat.equals("in-order"))
            throw new IllegalArgumentException(newFormat 
                                               + " evaluation is not supported yet");

        /** Transition to the designated UninitializedState. */
        context.state(uninitializedStateFactory.makeUninitializedState(newFormat));
    }

    

    /** 
     * A state factory responsible for building uninitilized
     * states.
     */        
    private static UninitializedStateFactory uninitializedStateFactory = 
        new UninitializedStateFactory();

    

    

   
    
  	  
    
}
