package com.example.expressiontree;

class InOrderUninitializedState extends UninitializedState {
    /** 
     * Process the @a expression using an in-order interpreter
     * and update the state of the @a context to the @a
     * InOrderInitializedState.
     */
    public InOrderUninitializedState() {
        super();
    }

    /** 
     * Process the @a expression using a in-order interpreter
     * and update the state of @a treeOps to the @a
     * InOrderInitializedState.
     */
    void makeTree(TreeOps treeOps, String inputExpression) {
        /**
         * Use the Interpreter and Builder patterns to create
         * the expression tree designated by user input.
         */
        treeOps.tree(treeOps.interpreter().interpret(inputExpression));

        /** Transition to the InOrderInitializedState. */
        treeOps.state(new InOrderInitializedState());
    }
}