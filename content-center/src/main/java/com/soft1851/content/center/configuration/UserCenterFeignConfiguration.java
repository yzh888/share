package com.soft1851.content.center.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level(){
        //打印所有细节Fegin
        return Logger.Level.FULL;
    }
}
