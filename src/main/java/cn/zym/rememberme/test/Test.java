package cn.zym.rememberme.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.codec.Hex;
import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @ClassName Test
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/7 12:19
 * @Version 1.0
 */
@SpringBootTest
public class Test {

    @org.junit.Test
    public void base64Decode() {
        String s = new String(Base64.getDecoder().decode("enltOjE1OTAwMzU3MDc0NTk6OWMzOTI3N2RlMTY2NmI2YWQ4YmY4NzM0ODg3YjRlZjE"));
        System.out.println("decode="+s);

        String data = "zym" + ":" + "1590035707459" + ":" + "123" + ":" + "zym";

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var8) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        //计算加盐散列值
        String encode =  new String(Hex.encode(digest.digest(data.getBytes())));
        System.out.println("encode="+encode);

        String s2 = new String(Base64.getDecoder().decode("MG14bHRweUR1bSUyQmQ2eGM4UHJ3Uk1nJTNEJTNEOnpwSDNzSEVMaHRXZ1ZYYTlFUmU3Y1ElM0QlM1Q"));
        System.out.println("s2="+s2);

    }

}
