package com.example.expressiontree;

/** 
 * This interface uses the Command pattern to create @a Visitor
 * implementations at runtime.
 */
interface IVisitorFactoryCommand {
    public Visitor execute();
}