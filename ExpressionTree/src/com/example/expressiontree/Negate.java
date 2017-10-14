package com.example.expressiontree;

/**
 * @class Negate
 *
 * @brief Defines a node in the parse tree for unary minus
 *        operator non-terminal expression.
 */
class Negate extends UnaryOperator {
    /** Ctor */
    public Negate() {
        super(null, Interpreter.negatePrecedence);
    }

    /** Adds precedence to its current value. */
    public int addPrecedence(int accumulatedPrecedence) {
        return precedence =
            Interpreter.negatePrecedence + accumulatedPrecedence;
    }

    /** Method for building a @a Negate node. */
    ComponentNode build() {
        return new CompositeNegateNode(right.build());
    }

    /** Returns the current precedence. */
    public int precedence() {
        return precedence;
    }
}