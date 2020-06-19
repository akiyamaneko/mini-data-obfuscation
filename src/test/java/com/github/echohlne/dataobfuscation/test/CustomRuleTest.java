package com.github.echohlne.dataobfuscation.test;


import com.github.echohlne.dataobfuscation.api.DataObfuscation;
import com.github.echohlne.dataobfuscation.bean.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomRuleTest {
    @Test
    public void testCustomRule() {
        Address detailAddress = new Address("北京市海淀区西二旗中路", "100000");
        Address detailAddressAfterObfuscate = DataObfuscation.obfuscateData(detailAddress);
        assertEquals(detailAddressAfterObfuscate.getDetailAddress(), "北京市********");

        Address simpleAddress = new Address("北京市", "100000");
        Address simpleAddressAfterObfuscate = DataObfuscation.obfuscateData(simpleAddress);
        assertEquals(simpleAddress.getDetailAddress(), simpleAddressAfterObfuscate.getDetailAddress());
    }
}
