package com.github.echohlne.dataobfuscation.strategy;



import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class NameStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String chineseName = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(chineseName)) {
            return "";
        }
        char[] chineseNameCharSource = chineseName.toCharArray();
        for (int i = 1; i < chineseNameCharSource.length; i++) {
            chineseNameCharSource[i] = '*';
        }
        return new String(chineseNameCharSource);
    }
}
