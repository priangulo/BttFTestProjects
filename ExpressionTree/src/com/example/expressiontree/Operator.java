package com.example.expressiontree;

/**
 * @class Operator
 *
 * @brief Defines a base class in the parse tree for operator
 *        non-terminal expressions.
 */
public abstract class Operator extends Symbol {
    /** Ctor */
    Operator(Symbol left,
             Symbol right,
             int precedence) {
        super(left, right, precedence);
    }
}