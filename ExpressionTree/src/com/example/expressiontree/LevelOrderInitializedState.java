package com.example.expressiontree;

import com.example.expressiontree.LevelOrderUninitializedState;

/**
 * A state formated level order and containing an expression
 * tree.
 */
public class LevelOrderInitializedState extends LevelOrderUninitializedState {
    public LevelOrderInitializedState() {
    }

    /**
     * Print the current expression tree in the @a context
     * using the designed @a format.
     */
    void print(TreeOps context, String format) {
        State.printTree(context.tree(), format);
    }
	  	
    /** 
     * Evaluate the yield of the current expression tree
     * in the @a context using the designed @a format.
     */
    void evaluate(TreeOps context, String format) {
        throw new IllegalArgumentException("LevelOrderInitializedState.evaluate() not yet implemented");
    }
}
