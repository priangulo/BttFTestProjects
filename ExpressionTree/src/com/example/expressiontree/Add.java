package com.example.expressiontree;

/**
 * @class Add
 *
 * @brief Defines a node in the parse tree for the binary add
 *        operator non-terminal expression.
 */
class Add extends Operator {
    /** Ctor */
    public Add() {
        super(null, null, Interpreter.addSubPrecedence);
    }

    /** Adds Precedence to its current value. */
    public int addPrecedence(int accumulatedPrecedence) {
        return precedence =
            Interpreter.addSubPrecedence + accumulatedPrecedence;
    }

    /** Method for building an @a Add node. */
    ComponentNode build() {
        return new CompositeAddNode(left.build(),
                                    right.build());
    }

    /** Returns the current precedence. */
    public int precedence() {
        return precedence;
    }
}