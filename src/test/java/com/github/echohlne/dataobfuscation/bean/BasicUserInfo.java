package com.github.echohlne.dataobfuscation.bean;


import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.github.echohlne.dataobfuscation.strategy.*;

import java.util.List;

public class BasicUserInfo {
    @Strategy(type = IDCardStrategy.class)
    private String idCard;

    @Strategy(type = NameStrategy.class)
    private String name;

    @Strategy(type = EmailStrategy.class)
    private String email;

    @Strategy(type = MobilePhoneNumberStrategy.class)
    private String mobilePhone;

    @Strategy(type = BankCardStrategy.class)
    private String bankCard;

    @Strategy(type = NameStrategy.class)
    private List<String> schoolList;

    public List<String> getSchoolList() {
        return schoolList;
    }


    public BasicUserInfo(String name, String idCard, String mobilePhone, String email, String bankCard, List<String> schoolList) {
        this.name = name;
        this.idCard = idCard;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.bankCard = bankCard;
        this.schoolList = schoolList;
    }

    public String getBankCard() {
        return bankCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }

    @Override
    public String toString() {
        return "BasicUserInfo{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", schoolList=" + schoolList +
                '}';
    }
}
