package me.fixeddev.commandflow.annotated.annotation;

import me.fixeddev.commandflow.annotated.CommandClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation has the purpose to allow multiple layers of subcommands
 * on parametric commands. This annotation will register the specified command classes
 * as subcommands for the annotated command class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubCommandClasses {
    Class<? extends CommandClass>[] value();
}
