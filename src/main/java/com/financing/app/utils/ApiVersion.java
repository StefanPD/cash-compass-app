package com.financing.app.utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
public @interface ApiVersion {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}
