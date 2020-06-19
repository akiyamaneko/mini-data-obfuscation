package com.github.echohlne.dataobfuscation.plugin;


import com.github.echohlne.dataobfuscation.meta.interfaces.IRule;

import java.util.Objects;

public class AddressNameRule implements IRule {
    @Override
    public boolean accept(Object dst) {
        return Objects.toString(dst).length() >= 5;
    }
}
