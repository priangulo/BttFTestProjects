package com.example.expressiontree;

import java.util.HashMap;

/**
 * @class UninitializedStateFactory
 * 
 * @brief Implementation of a factory pattern that dynamically
 *        allocates the appropriate @a State object. 
 * 
 *        This class is a variant of the Abstract Factory
 *        pattern that has a set of related factory methods
 *        but which doesn't use inheritance.
 */
public class UninitializedStateFactory {
    /**
     * A HashMap that maps user format string requests to the
     * corresponding UninitializedState implementations.
     */
    private HashMap<String, State> uninitializedStateMap =
        new HashMap<String, State>();
	 	 
    /** Ctor */
    UninitializedStateFactory() {
        uninitializedStateMap.put
            ("in-order",
             new InOrderUninitializedState());
        uninitializedStateMap.put
            ("pre-order",
             new PreOrderUninitializedState());
        uninitializedStateMap.put
            ("post-order",
             new PostOrderUninitializedState());
        uninitializedStateMap.put
            ("level-order",
             new LevelOrderUninitializedState());
    }
			
    /** 
     * Dynamically allocate a new @a State object based on the
     * designated @a format.
     */
    public State makeUninitializedState(String formatRequest) {
        /** 
         * Try to find the pre-allocated UninitializedState
         * implementation. 
         */
        State state = uninitializedStateMap.get(formatRequest);
				
        if(state != null)
            /** If we find it then return it. */
            return state;
        else 
            /** 
             * Otherwise, the user gave an unknown request, so throw
             * an exception.
             */
            throw new IllegalArgumentException(formatRequest 
                                               + "is not a supported format");
    }
}
