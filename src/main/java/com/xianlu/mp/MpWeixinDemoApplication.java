package com.xianlu.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import xyz.erupt.core.annotation.EruptScan;

@EntityScan({ "xyz.erupt", "com.xianlu.mp" })
@EruptScan({ "xyz.erupt", "com.xianlu.mp" })
@SpringBootApplication
public class MpWeixinDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpWeixinDemoApplication.class, args);
    }

}
