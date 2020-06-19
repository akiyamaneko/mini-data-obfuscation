package com.github.echohlne.dataobfuscation.strategy;



import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class AddressStrategy implements IStrategy {

    public Object mask(Object src) {
        String address = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(address)) {
            return "";
        }
        char[] addressCharSource = address.toCharArray();
        if (addressCharSource.length <= 3) {
            return address;
        }
        for (int i = 3; i < addressCharSource.length; i++) {
            addressCharSource[i] = '*';
        }
        return new String(addressCharSource);
    }
}
