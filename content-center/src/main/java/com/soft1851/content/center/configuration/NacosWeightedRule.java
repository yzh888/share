package com.soft1851.content.center.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    //服务发现的属性，发现名称空间实例

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig){
        //读取配置文件，并初始化 NacosWeightedRule
    }

    @Override
    public Server choose(Object key){
        try{
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();

            //想要请求的微服务的名称
            String name = loadBalancer.getName();

            //拿到服务发现的相关api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //nacos client 自动通过基于权重的负载均衡算法，给我们选择一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);
             log.info("选择的实例是：port = {},instance = {}", instance.getPort(), instance);
             return new NacosServer(instance);//把权重和负载均衡结合
        }catch (NacosException e){
            return null;
        }
    }
}
