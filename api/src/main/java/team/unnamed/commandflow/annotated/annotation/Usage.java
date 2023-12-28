package team.unnamed.commandflow.annotated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Changes the usage for the command annotated with this annotation, allowing custom usages instead of the default
 * auto generated ones.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Usage {

    /**
     * @return A string representing the usage for this command.
     */
    String value();

}
