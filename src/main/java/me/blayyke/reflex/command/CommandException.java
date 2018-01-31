package me.blayyke.reflex.command;

public class CommandException extends RuntimeException {
    public CommandException(Exception e) {
        super(e);
    }
}