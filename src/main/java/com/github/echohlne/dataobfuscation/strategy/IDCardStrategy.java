package com.github.echohlne.dataobfuscation.strategy;

import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class IDCardStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String idCard = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(idCard)) {
            return "";
        }
        char[] idCardSource = idCard.toCharArray();
        for (int i = 6; i < idCardSource.length; i++) {
            idCardSource[i] = '*';
        }
        return new String(idCardSource);
    }
}
