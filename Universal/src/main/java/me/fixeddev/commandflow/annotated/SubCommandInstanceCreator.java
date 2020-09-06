package me.fixeddev.commandflow.annotated;

public interface SubCommandInstanceCreator {
    CommandClass createInstance(Class<?> clazz, CommandClass parent);
}
