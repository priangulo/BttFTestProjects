package com.example.expressiontree;

/** 
 * This interface uses the Command pattern to create @a Platform
 * implementations at runtime.
 */
interface IPlatformFactoryCommand {
    public Platform execute();
}