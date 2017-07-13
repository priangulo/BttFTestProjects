package com.example.expressiontree;

import com.example.expressiontree.InOrderUninitializedState;

/** 
 * A state formatted in-order and containing an expression
 * tree.
 */
public class InOrderInitializedState extends InOrderUninitializedState {
    public InOrderInitializedState() {
    }

    /** 
     * Print the current expression tree in the @a context
     * using the designed @a format.
     */
    void print(TreeOps context, String format) {
        State.printTree(context.tree(), format);
    }

    /** 
     * Evaluate the yield of the current expression tree in the @a
     * context using the designed @a format.
     */
    void evaluate(TreeOps context, String format) {
        State.evaluateTree(context.tree(), format);
    }
}