package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.LoadBalancedConnection;
import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.net.URI;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    //Զ������spring security �������Ƽ���jwt
    @Test
    public void testClient(){
        //��eureka�л�ȡ��֤����ĵ�ַ����Ϊspring security����֤�����У�
        //��eureka�л�ȡ��֤�����һ��ʵ����ַ
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        //�˵�ַ����http://ip:port
        URI uri = serviceInstance.getUri();
        //��������ĵ�ַhttp://localhost:40400/auth/oauth/token
        String authUrl = uri+"/auth/oauth/token";

        //����header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic("XcWebApp", "XcWebApp");
        header.add("Authorization",httpBasic);

        //����body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","1234");

        HttpEntity<MultiValueMap> multiValueMapHttpEntity = new HttpEntity<>(body,header);


        //(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,Class<T> responseType, Object... uriVariables
        //����restTemplateԶ�̵��õ�ʱ�򣬶�400��401���ñ�����ȷ��������
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);

        //����������Ϣ
        Map bodyMap = exchange.getBody();
        System.out.println(bodyMap);
    }

    //��ȡhttpbasic�Ĵ�
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //��������base64����
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

    @Test
    public void testPasswordEncoder(){
        //ԭʼ����
        String password = "111111";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //ʹ��BCrypt���ܣ�ÿ�μ���ʹ��һ�������
        for(int i = 0;i<10;i++){
            String encode = bCryptPasswordEncoder.encode(password);
            System.out.println(encode);
            //У��
            boolean matches = bCryptPasswordEncoder.matches(password, encode);
            System.out.println(matches);
        }
    }

}
