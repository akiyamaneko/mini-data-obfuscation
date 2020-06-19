# mini-data-obfuscation

mini-data-obfuscation is a common security requirement. This project provides annotation based method and built-in common obfuscation method, which is convenient for developer. Developers can also customize annotations based on their actual needs.

## License

This mini-data-obfuscation is Apache 2 Licensed.

## Building

```java
mvn clean package
```

## Usage Examples

### ## Use built-in annotations

```java
import com.github.echohlne.dataobfuscation.api.DataObfuscation;
import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.github.echohlne.dataobfuscation.strategy.*;

public class UserInfo {

    @Strategy(type = NameStrategy.class)
    private String username;

    @Strategy(type =  IDCardStrategy.class)
    private String idCard;

    @Strategy(type =  PasswordStrategy.class)
    private String password;

    @Strategy(type =  EmailStrategy.class)
    private String email;

    @Strategy(type =  MobilePhoneNumberStrategy.class)
    private String mobilePhone;

    @Strategy(type = LinePhoneNumberStrategy.class)
    private String linePhone;

    public UserInfo(String username, String idCard, String password, String email, String mobilePhone, String linePhone) {
        this.username = username;
        this.idCard = idCard;
        this.password = password;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.linePhone = linePhone;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", idCard='" + idCard + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", linePhone='" + linePhone + '\'' +
                '}';
    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("海淀吴彦祖", "123456789012345678", "password123456", "echohlne@gmail.com", "12345678901", "012481812345");
        System.out.println("before data obfuscation original data is:" + userInfo.toString());
        System.out.println("after data obfuscation data after clone is:" + DataObfuscation.obfuscateData(userInfo));
        System.out.println("after data obfuscation original data is:" + userInfo.toString());
    }
}
```

The output is as follows the original data is not affected and can be used for other operations:

```shell
before data obfuscation original data is:UserInfo{username='海淀吴彦祖', idCard='123456789012345678', password='password123456', email='echohlne@gmail.com', mobilePhone='12345678901', linePhone='012481812345'}
after data obfuscation data after clone is:UserInfo{username='海****', idCard='123456************', password='', email='echo****@*****.com', mobilePhone='123****8901', linePhone='********2345'}
after data obfuscation original data is:UserInfo{username='海淀吴彦祖', idCard='123456789012345678', password='password123456', email='echohlne@gmail.com', mobilePhone='12345678901', linePhone='012481812345'}
```

## Customize New Strategy

Please just look at the `AddressStrategy.java` in `package com.github.echohlne.dataobfuscation.strategy`, you can just custom new strategy by implementing the interface:`IStrategy`.

```java
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
```



# Customize New Rule

You can also customize rules to define when annotations take effect line belows:

```java
public class AddressNameRule implements IRule {
    @Override
    public boolean accept(Object dst) {
        return Objects.toString(dst).length() >= 5;
    }
}

public class Address {
    @Strategy(type = AddressStrategy.class)
    // add the custom rule.
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

@Test
    public void testCustomRule() {
        // strategy takes effect.
        Address detailAddress = new Address("北京市海淀区西二旗中路", "100000");
        Address detailAddressAfterObfuscate = DataObfuscation.obfuscateData(detailAddress);
        assertEquals(detailAddressAfterObfuscate.getDetailAddress(), "北京市********");

        // strategy has no effect.
        Address simpleAddress = new Address("北京市", "100000");
        Address simpleAddressAfterObfuscate = DataObfuscation.obfuscateData(simpleAddress);
        assertEquals(simpleAddress.getDetailAddress(), simpleAddressAfterObfuscate.getDetailAddress());
    }

```



## Support Collection and Array

Now it supports collection and list nested custom Java bean or like String objects. scenarios. For specific examples,  please refer: https://github.com/echohlne/mini-data-obfuscation/tree/master/src/test/java/com/github/echohlne/dataobfuscation



## next step

Make functions more flexible and easy to use, and support map and other types