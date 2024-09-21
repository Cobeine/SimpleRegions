package me.cobeine.regions.api.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubCommandInfo {

    String name();

    String syntax() default "";

    LengthCheckType length() default LengthCheckType.EQUAL;

    String description() default "";

    String permission() default "";

    int requiredLength() default 0;
}