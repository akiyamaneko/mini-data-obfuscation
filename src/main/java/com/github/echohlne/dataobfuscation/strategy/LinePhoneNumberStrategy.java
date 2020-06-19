package com.github.echohlne.dataobfuscation.strategy;


import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class LinePhoneNumberStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String linePhone = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(linePhone)) {
            return "";
        }
        char[] linePhoneCharSource = linePhone.toCharArray();
        if (linePhoneCharSource.length < 4) {
            return linePhone;
        }
        for (int i = 0; i < linePhone.length() - 4; i++) {
            linePhoneCharSource[i] = '*';
        }
        return new String(linePhoneCharSource);
    }
}
