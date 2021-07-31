package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {
    /**
     *�˽⼴�ɣ�spring security���+oauth2Э�� ���԰������ǿ��ٻ�ȡ�ⲿ������
     */

    //����jwt����
    @Test
    public void testCreateJwt(){
        //��Կ���ļ�
        String keystore = "xc.keystore";
        //��Կ�������
        String keystore_password = "xuechengkeystore";

        //��Կ���ļ���·��
        ClassPathResource classPathResource = new ClassPathResource( keystore);
        //��Կ����
        String alias = "xckey";
        //��Կ�ķ�������
        String key_password = "xuecheng";
        //��Կ����
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());
        //��Կ�ԣ���Կ��˽Կ��
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //��ȡ˽Կ
        RSAPrivateKey aPrivate = (RSAPrivateKey)keyPair.getPrivate();
        //jwt���Ƶ�����
        HashMap<String, String> body = new HashMap<>();
        body.put("name","itcast");
        String bodyString = JSON.toJSONString(body);
        //����jwt����
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(aPrivate));
        //����jwt���Ʊ���
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    //У��jwt����
    @Test
    public void testVerify(){
        //��Կ
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //jwt����
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE2Mjc2ODUxNjgsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsImNvdXJzZV9waWNfbGlzdCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6ImU3YjAwYjUzLTFiMTQtNGNhMC04OThhLTM5NTZmMzEyNGE3NCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.OurJcJ777UcGWoXXcMVkeMu0uKPQ5Vm2X_EpPDBBkWKufgqT_wmhvSV8P8RfrXc5RULHaaKkc4O_msvBYHbzckUSJ06LCder9ssGEoMYkeYJIEYfen50uIk-FgCXAZysHukQrDkdbCROsSrnoDi-g1RH9GDJ5ztRoy4MK8o1bJHnPbvqyjXobt5L-n8Tk3GVmdxt72BzJG2cvTTHTDJP8HPeGIDknqd_33E8fucsjgT-zjB25hh8B2HwwXliD4WcAhwJTezTW4Sl61LZgrOihYk3Fz51ORHXGF8ndgPq9fkIAH-bEAeLJtZVvTlgfZHfIb4QYFPTAimN-Co7wkJ2ug";
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //�õ�jwt�������Զ��������
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
