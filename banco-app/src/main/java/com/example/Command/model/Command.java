package com.example.Command.model;

public interface Command {
    
    void execute();
    
    void undo();
    
    String getCommandName();
    
    String getCommandDetails();
}
