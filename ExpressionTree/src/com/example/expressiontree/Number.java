package com.example.expressiontree;

/**
 * @class Symbol
 *
 * @brief Defines a node in the parse tree for number terminal
 *        expressions.
 */
class Number extends Symbol {
    /** Value of Number. */
    public int item;

    /** Ctor */
    public Number(String input) {
        super(null, null, Interpreter.numberPrecedence);
        item = Integer.parseInt(input);
    }

    /** Ctor */
    public Number(int input) {
        super(null, null, Interpreter.numberPrecedence);
        item = input;
    }

    /** 
     * Adds numberPrecedence to the current accumulatedPrecedence
     * value.
     */
    public int addPrecedence(int accumulatedPrecedence) {
        return precedence = 
            Interpreter.numberPrecedence + accumulatedPrecedence;
    }

    /** 
     * Method for returning precedence level (higher value means
     * higher precedence).
     */
    public int precedence() {
        return precedence;
    }

    /** Builds a @a LeadNode. */
    ComponentNode build() {
        return new LeafNode(item);
    }
}