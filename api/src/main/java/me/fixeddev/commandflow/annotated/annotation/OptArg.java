package me.fixeddev.commandflow.annotated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The purpose of this annotation is to define the default value of a parameter
 * in an {@link me.fixeddev.commandflow.command.Command}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OptArg {

    String[] value() default {};

}
