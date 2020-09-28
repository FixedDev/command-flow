package me.fixeddev.commandflow.annotated;

public interface SubCommandInstanceCreator {
    CommandClass createInstance(Class<? extends CommandClass> clazz, CommandClass parent);
}
