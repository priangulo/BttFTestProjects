package com.example.expressiontree;

/** 
 * This interface uses the Command pattern to create @a
 * UserCommand implementations at runtime.
 */
public interface IUserCommandFactoryCommand {
    public UserCommand execute(String param);
}