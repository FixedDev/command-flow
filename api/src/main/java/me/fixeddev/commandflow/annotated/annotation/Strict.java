package me.fixeddev.commandflow.annotated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The sole purpose of this annotation is to allow the creation of strict optional arguments, which are not the
 * old default behaviour of the command manager
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Strict {
}
