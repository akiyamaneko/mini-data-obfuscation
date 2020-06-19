package com.github.echohlne.dataobfuscation.strategy;



import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class EmailStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String email = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(email)) {
            return "";
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 0) {
            return email;
        }
        char[] emailCharSource = email.toCharArray();
        for (int i = (atIndex > 4 ? 4 : 0); i <= atIndex - 1; i++) {
            emailCharSource[i] = '*';
        }

        int dotIndex = email.indexOf(".");
        if (dotIndex > atIndex) {
            for (int i = atIndex + 1; i < dotIndex; i++) {
                emailCharSource[i] = '*';
            }
        }

        return new String(emailCharSource);
    }
}
