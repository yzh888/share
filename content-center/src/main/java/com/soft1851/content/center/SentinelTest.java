package com.soft1851.content.center;

import org.springframework.web.client.RestTemplate;

public class SentinelTest {
    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i<10;i++){
            String object = restTemplate.getForObject("http://localhost:8089/test/byResource", String.class);
            System.out.println(object);
            //Thread.sleep(200);
        }
    }
}
