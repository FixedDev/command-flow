package me.fixeddev.commandflow.annotated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParentArg {

    /**
     * The index used to get this argument CommandPart value, 0 by default
     * but can be changed if more than 1 part with the same name is present
     *
     * @return An integer representing the index of this argument's CommandPart
     */
    int value() default 0;

}
