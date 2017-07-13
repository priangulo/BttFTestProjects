package com.example.expressiontree;

import java.util.Iterator;

/**
 * @class State
 * 
 * @brief Implementation of the State pattern that is used to define
 *        the various states that affect how users operations are
 *        processed.  Plays the role of the "State" base class in the
 *        State pattern that is used as the basis for the subclasses
 *        that actually define the various states.
 */
public class State {
    /**
     * A factory that creates the appropriate visitors.
     */
    private static VisitorFactory visitorFactory = 
        new VisitorFactory();

    /**
     * Throws an exception if called in the wrong state.
     */
    void format(TreeOps context, String newFormat) {
        throw new IllegalStateException("State.format() called in invalid state");
    }
	  
    /** 
     * Throws an exception if called in the wrong state.
     */
    void makeTree(TreeOps context,
                  String expression) {
        throw new IllegalStateException("State.makeTree() called in invalid state");
    }
	  
    /** 
     * Throws an exception if called in the wrong state.
     */
    void print(TreeOps context,
               String format) {
        throw new IllegalStateException("State.print() called in invalid state");
    }
	  
    /** 
     * Throws an exception if called in the wrong state.
     */
    void evaluate(TreeOps context,
                  String format) {
        throw new IllegalStateException("State.evaluate() called in invalid state");
    }
    
    /** 
     * Print the operators and operands of the @a tree using the
     * designated @a traversalOrder.
     */
    static void printTree(ExpressionTree tree,
                          String traversalOrder) {		  
        if (traversalOrder.equals(""))
            /** 
             * Default to in-order if user doesn't explicitly request
             * a print order.
             */
            traversalOrder = "in-order";

        /**
         * Note the high pattern density in the code below, which uses
         * of the Factory Method, Iterator, Bridge, Strategy, and
         * Visitor patterns.
         */

        /** Create the PrintVisitor using a factory. */
        Visitor printVisitor = visitorFactory.makeVisitor("print"); 
        
        /** 
         * Iterate through all nodes in the expression tree and accept
         * the printVisitor to print each type of node.
         */
        for(Iterator<ExpressionTree> it = tree.makeIterator(traversalOrder);
            it.hasNext();
            )
            it.next().accept(printVisitor);
    }
	  	
    /** 
     * Evaluate and print the yield of the @a tree using the
     * designated @a traversalOrder.
     */
    static void evaluateTree(ExpressionTree tree,
                             String traversalOrder) {
        if (traversalOrder.equals(""))
            /** 
             * Default to post-order if user doesn't explicitly
             * request an eval order.
             */
            traversalOrder = "post-order";
        else if (!traversalOrder.equals("post-order"))
            throw new IllegalArgumentException(traversalOrder + " evaluation is not supported yet");

        /**
         * Note the high pattern density in the code below, which uses
         * of the Factory Method, Iterator, Bridge, Strategy, and
         * Visitor patterns.
         */

        /** Create the EvaluationVisitor using a factory. */
        Visitor evalVisitor = visitorFactory.makeVisitor("eval");
  
        /** 
         * Iterate through all nodes in the expression tree and accept
         * the evalVisitor to evaluate each type of node.
         */
        for(Iterator<ExpressionTree> it = tree.makeIterator(traversalOrder);
            it.hasNext();
            )
            it.next().accept(evalVisitor);

        Integer total = ((EvaluationVisitor) evalVisitor).total();

        // Use the platform strategy to printout the result.
        Platform.instance().outputLine(total.toString());
    }
}
