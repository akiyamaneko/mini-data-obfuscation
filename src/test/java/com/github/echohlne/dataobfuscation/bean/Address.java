package com.github.echohlne.dataobfuscation.bean;


import com.github.echohlne.dataobfuscation.meta.annotation.Rule;
import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.github.echohlne.dataobfuscation.plugin.AddressNameRule;
import com.github.echohlne.dataobfuscation.strategy.AddressStrategy;

public class Address {
    @Strategy(type = AddressStrategy.class)
    @Rule(accept = AddressNameRule.class)
    private String detailAddress;
    private String zipCode;

    public String getDetailAddress() {
        return detailAddress;
    }


    public String getZipCode() {
        return zipCode;
    }

    public Address(String detailAddress, String zipCode) {
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }


    @Override
    public String toString() {
        return "com.github.echohlne.datamask.bean.Address{" +
                "addressName='" + detailAddress + '\'' +
                ", addressCode='" + zipCode + '\'' +
                '}';
    }
}
