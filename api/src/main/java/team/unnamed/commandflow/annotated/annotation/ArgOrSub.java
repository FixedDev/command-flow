package team.unnamed.commandflow.annotated.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ArgOrSub {

    /**
     * If the order of parsing should be reversed to first try parsing the subcommand and later on the arguments
     * @return A boolean indicating whether the order of parsing should be reversed.
     */
    boolean value() default false;

}
