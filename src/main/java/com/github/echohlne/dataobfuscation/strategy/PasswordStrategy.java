package com.github.echohlne.dataobfuscation.strategy;


import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;

public class PasswordStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        return "";
    }
}
