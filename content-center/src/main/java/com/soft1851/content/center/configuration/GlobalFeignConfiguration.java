package com.soft1851.content.center.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;


public class GlobalFeignConfiguration {
    @Bean
    public Logger.Level level(){
        //让feign打印所有请求的细节
        return Logger.Level.FULL;
    }
}
