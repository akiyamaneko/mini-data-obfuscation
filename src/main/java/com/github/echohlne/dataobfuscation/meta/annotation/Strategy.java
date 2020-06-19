package com.github.echohlne.dataobfuscation.meta.annotation;




import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {
    Class<? extends IStrategy> type();
}
