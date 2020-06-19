package com.github.echohlne.dataobfuscation.strategy;



import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.google.common.base.Strings;

public class BankCardStrategy implements IStrategy {
    @Override
    public Object mask(Object src) {
        String bankCard = ClassesUtils.getObjectStr(src);
        if (Strings.isNullOrEmpty(bankCard)) {
            return "";
        }
        char[] bankCardCharSource = bankCard.toCharArray();
        if (bankCardCharSource.length < 6) {
            return bankCard;
        }
        StringBuilder builder = new StringBuilder().append(new String(bankCardCharSource, 0, 6));
        if (bankCardCharSource.length > 10) {
            builder.append(Strings.repeat("*", bankCardCharSource.length - 10));

        }
        builder.append(new String(bankCardCharSource, bankCardCharSource.length - 4, 4));
        return builder.toString();
    }
}
