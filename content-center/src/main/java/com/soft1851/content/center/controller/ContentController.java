package com.soft1851.content.center.controller;

import com.soft1851.content.center.domain.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ApiIgnore
@RequestMapping(value = "/content")
public class ContentController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/hello")
    public String testNacos() {
        return "Hello, Nacos!!!";
    }

    @GetMapping(value = "/call")
    public String getHello() {
        return restTemplate.getForObject("http://localhost:8021/test/hello",String.class);
    }

    @GetMapping("/discovery")
    public List<ServiceInstance> getInstances() {
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("/call/hello")
    public String callUserCenter() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        String targetUrl = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/test/hello")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("当前没有实例！"));
        //随机1
//        ServiceInstance serviceInstance = instances.get(new Random().nextInt(instances.size()));
//        String targetUrl = serviceInstance.getUri() + "/test/hello";

        //随机2
//        Random random = new Random();
//        String targetUrl = instances.stream()
//                .map(instance -> instance.getUri().toString() + "/test/hello")
//                .collect(Collectors.toList()).get(random.nextInt(instances.size()));
        //所有实例url集合
        List<String> targetUrls = instances.stream()
                .map(instance -> instance.getUri().toString() + "/test/hello")
                .collect(Collectors.toList());
        int i = ThreadLocalRandom.current().nextInt(targetUrls.size());

        log.info("请求的目标地址：{}",targetUrls.get(i));


        return restTemplate.getForObject(targetUrls.get(i),String.class);
    }

    @GetMapping(value = "/call/ribbon")
    public String callByRibbon(){
        return restTemplate.getForObject("http://user-center/test/hello",String.class);
    }

    @GetMapping("/findById/{id}")
    public Student fingById(@PathVariable("id") long id){
        return restTemplate.getForObject("http://localhost:8028/student/findById/{id}",Student.class,id);
    }
}