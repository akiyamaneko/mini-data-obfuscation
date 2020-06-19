package com.github.echohlne.dataobfuscation.strategy;


import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class MobilePhoneNumberStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String mobilePhone = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(mobilePhone)) {
            return "";
        }
        int length = mobilePhone.length();
        if (length <= 3) {
            return mobilePhone;
        }
        String prefix = mobilePhone.substring(0, 3);
        String suffix = mobilePhone.substring(length - 4);
        String middle = length <= 7 ? "" : Strings.repeat("*", length - 7);
        return prefix + middle + suffix;
    }
}
