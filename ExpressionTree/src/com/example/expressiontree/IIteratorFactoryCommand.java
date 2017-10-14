package com.example.expressiontree;

import java.util.Iterator;

/** 
 * This interface uses the Command pattern to create @a Iterator
 * implementations at runtime.
 */
public interface IIteratorFactoryCommand {
    public Iterator<ExpressionTree> execute(ExpressionTree tree);
}