package com.github.echohlne.dataobfuscation.bean;

import java.util.List;

public class Resume {
    private String desiredPosition;

    private String personalBlog;

    private String github;

    private Address homeAddress;

    private BasicUserInfo basicUserInfo;

    private List<Company> historyCompanyList;

    public String getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(String desiredPosition) {
        this.desiredPosition = desiredPosition;
    }


    public String getPersonalBlog() {
        return personalBlog;
    }

    public void setPersonalBlog(String personalBlog) {
        this.personalBlog = personalBlog;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public BasicUserInfo getBasicUserInfo() {
        return basicUserInfo;
    }

    public void setBasicUserInfo(BasicUserInfo basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
    }

    public List<Company> getHistoryCompanyList() {
        return historyCompanyList;
    }

    public void setHistoryCompanyList(List<Company> historyCompanyList) {
        this.historyCompanyList = historyCompanyList;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString() {
        return "com.github.echohlne.datamask.bean.Resume{" +
                "hopePosition='" + desiredPosition + '\'' +
                ", personalBlog='" + personalBlog + '\'' +
                ", github='" + github + '\'' +
                ", homeAddress=" + homeAddress +
                ", basicUserInfo=" + basicUserInfo +
                ", historyCompanyList=" + historyCompanyList +
                '}';
    }
}
