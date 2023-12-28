package team.unnamed.commandflow.annotated.annotation;

import team.unnamed.commandflow.command.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The purpose of this annotation is to define the default value of a parameter
 * in an {@link Command}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OptArg {

    String[] value() default {};

}
