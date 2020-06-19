package com.github.echohlne.dataobfuscation.bean;


import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.github.echohlne.dataobfuscation.strategy.LinePhoneNumberStrategy;

public class Company {

    private String companyName;
    private Address address;
    @Strategy(type = LinePhoneNumberStrategy.class)
    private String linePhone;
    private String companyWebSite;

    public Company(String companyName, String linePhone, String companyWebSite, Address address) {
        this.companyName = companyName;
        this.linePhone = linePhone;
        this.companyWebSite = companyWebSite;
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLinePhone() {
        return linePhone;
    }

    public void setLinePhone(String linePhone) {
        this.linePhone = linePhone;
    }

    public String getCompanyWebSite() {
        return companyWebSite;
    }

    public void setCompanyWebSite(String companyWebSite) {
        this.companyWebSite = companyWebSite;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", address=" + address +
                ", linePhone='" + linePhone + '\'' +
                ", companyWebSite='" + companyWebSite + '\'' +
                '}';
    }
}
