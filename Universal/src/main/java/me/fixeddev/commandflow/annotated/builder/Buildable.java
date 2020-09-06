package me.fixeddev.commandflow.annotated.builder;

import me.fixeddev.commandflow.command.Command;

public interface Buildable {
    Command build();
}
