package com.example.expressiontree;

/**
 * A state formated pre-order without an expression tree.
 */
public class PreOrderUninitializedState extends UninitializedState {
    /** Ctor */
    public PreOrderUninitializedState() {
    }
    
    /**
     * Process the @a expression using a pre-order interpreter
     * and update the state of the @a context to the @a
     * PreOrderUninitializedState.
     */
    void makeTree(TreeOps context, String format) {
        throw new IllegalStateException("PreOrderUninitializedState.makeTree() not yet implemented");
    }
}