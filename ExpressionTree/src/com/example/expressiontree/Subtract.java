package com.example.expressiontree;

/**
 * @class Subtract
 *
 * @brief Defines a node in the parse tree for the binary subtract
 *        operator non-terminal expression.
 */
class Subtract extends Operator {
    /** Ctor */
    public Subtract() {
        super(null, null, Interpreter.addSubPrecedence);
    }

    /** Adds precedence to its current value. */
    public int addPrecedence(int accumulatedPrecedence) {
        return precedence =
            Interpreter.addSubPrecedence + accumulatedPrecedence;
    }

    /** Method for building a @a Subtract node. */
    ComponentNode build() {
        return new CompositeSubtractNode(left.build(),
                                         right.build());
    }

    /** Returns the current precedence. */
    public int precedence() {
        return precedence;
    }
}