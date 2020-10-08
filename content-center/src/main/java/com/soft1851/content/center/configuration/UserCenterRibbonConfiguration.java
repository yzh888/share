package com.soft1851.content.center.configuration;

import com.soft1851.content.center.ribbonconfig.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
//导入我自定义配置的ribbon规则
public class UserCenterRibbonConfiguration {
}
