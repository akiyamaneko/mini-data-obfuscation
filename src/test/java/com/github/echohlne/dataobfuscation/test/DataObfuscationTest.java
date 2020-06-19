package com.github.echohlne.dataobfuscation.test;


import com.github.echohlne.dataobfuscation.api.DataObfuscation;
import com.github.echohlne.dataobfuscation.bean.Address;
import com.github.echohlne.dataobfuscation.bean.BasicUserInfo;
import com.github.echohlne.dataobfuscation.bean.Company;
import com.github.echohlne.dataobfuscation.bean.Resume;
import com.github.echohlne.dataobfuscation.strategy.LinePhoneNumberStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataObfuscationTest {
    private Resume resume;

    @Before
    public void initResume() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add("珞珈山综合职业技术培训学院");
        schoolList.add("五山镇理工学院");
        schoolList.add("五道口男子体校");
        BasicUserInfo basicUserInfo = new BasicUserInfo("海淀周杰伦", "420982123456789012",
                "12345678901", "123456789@gmail.com", "1234567890123456789", schoolList);


        List<Company> companyList = new ArrayList<>();
        companyList.add(new Company("XXXXXXXX公司", "12345678", "https://www.xxxxxxx.com"
        , new Address("北京市XXXXXXXXXXX", "123456")));

        resume = new Resume();
        resume.setBasicUserInfo(basicUserInfo);
        resume.setDesiredPosition("大数据架构师");
        resume.setGithub("echohlne@gmail.com");
        resume.setHomeAddress(new Address("湖北省武汉市汉阳区", "430000"));
        resume.setHistoryCompanyList(companyList);
        resume.setPersonalBlog("https:www.hklmuaqmn.com");
    }

    @Test
        public void testAnnotation() {
        Resume copiedResume = DataObfuscation.obfuscateData(resume);
        System.out.println(copiedResume);
        assertEquals(copiedResume.getHomeAddress().getDetailAddress(), "湖北省******");
        assertEquals(copiedResume.getHistoryCompanyList().get(0).getLinePhone(), "****5678");
        assertEquals(copiedResume.getBasicUserInfo().getIdCard(), "420982************");
        assertEquals(copiedResume.getBasicUserInfo().getName(), "海****");
        assertEquals(copiedResume.getBasicUserInfo().getEmail(), "1234*****@*****.com");
        assertEquals(copiedResume.getBasicUserInfo().getMobilePhone(), "123****8901");
        assertEquals(copiedResume.getBasicUserInfo().getSchoolList().toString(), "[珞************, 五******, 五******]");
    }

    @Test
    public void testDirectObfuscationWithAPI() {
        String lineNumber = "12345678901";
        assertEquals(new LinePhoneNumberStrategy().mask(lineNumber), "*******8901");
    }
}
