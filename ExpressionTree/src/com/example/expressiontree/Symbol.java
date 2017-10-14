package com.example.expressiontree;

/**
 * @class Symbol
 *
 * @brief Base class for the various parse tree subclasses.
 */
abstract class Symbol {
    /** 
     * The following static consts set the precedence levels of
     * the various operations and operands.
     */

    /** Default precedence. */
    protected int precedence = 0;

    /** Left symbol. */
    protected Symbol left;

    /** Right symbol. */
    protected Symbol right;

    /** Ctor */
    public Symbol(Symbol left,
                  Symbol right,
                  int precedence) {
        this.precedence = precedence;
        this.left = left;
        this.right = right;
    }

    /** 
     * Method for returning precedence level (higher value means
     * higher precedence.
     */
    public int precedence() {
        return precedence;
    }

    /** Abstract method for adding precedence. */
    public abstract int addPrecedence(int accumulatedPrecedence);

    /** Abstract method for building a @a ComponentNode. */
    abstract ComponentNode build();
}