package com.github.echohlne.dataobfuscation.meta.interfaces;

public interface IRule {
    boolean accept(Object dst);
}
