package me.fixeddev.commandflow.annotated;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionInstanceCreator implements SubCommandInstanceCreator {
    @Override
    public CommandClass createInstance(Class<?> clazz, CommandClass parent) {
        try {
            Constructor<?> constructor;
            boolean useUpperClass = true;

            try {
                constructor = clazz.getConstructor(parent.getClass());
            } catch (NoSuchMethodException e) {
                constructor = clazz.getConstructor();
                useUpperClass = false;
            }

            boolean accessible = constructor.isAccessible();
            constructor.setAccessible(true);

            CommandClass instance;

            if (useUpperClass) {
                instance = (CommandClass) constructor.newInstance(parent);
            } else {
                instance = (CommandClass) constructor.newInstance();
            }
            constructor.setAccessible(accessible);

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
