package com.github.echohlne.dataobfuscation.meta.annotation;




import com.github.echohlne.dataobfuscation.meta.interfaces.IRule;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    Class<? extends IRule> accept();
}
