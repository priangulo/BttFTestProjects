package com.example.expressiontree;

/**
 * @class Multiply
 *
 * @brief Defines a node in the parse tree for the binary multiply
 *        operator non-terminal expression.
 */
class Multiply extends Operator {
    /** Ctor */
    public Multiply() {
        super(null, null, Interpreter.mulDivPrecedence);
    }

    /** Adds precedence to its current value. */
    public int addPrecedence(int accumulatedPrecedence) {
        return precedence =
            Interpreter.mulDivPrecedence + accumulatedPrecedence;
    }

    /** Method for building a @a Multiple node. */
    ComponentNode build() {
        return new CompositeMultiplyNode(left.build(),
                                         right.build());
    }

    /** Returns the precedence. */
    public int precedence() {
        return precedence;
    }
}