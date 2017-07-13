package com.example.expressiontree;

/** 
 * A state formated pre-order and containing an expression
 * tree.
 */
public class PreOrderInitializedState extends PreOrderUninitializedState {
    /** Ctor */
    public PreOrderInitializedState() {
    }

    /** 
     * Print the current expression tree in the @a context
     * using the designed @a format.
     */
    void print(TreeOps context, String format) {
        State.printTree(context.tree(), format);
    }
  	
    /** 
     * Evaluate the yield of the current expression tree in
     * the @a context using the designed @a format.
     */
    void evaluate(TreeOps context, String format) {
        throw new IllegalArgumentException("PreOrderInitializedState.evaluate() not yet implemented");
    }
}