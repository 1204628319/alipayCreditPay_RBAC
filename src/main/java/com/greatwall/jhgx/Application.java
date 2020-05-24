package com.greatwall.jhgx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author wangcan
 * @date 2019/11/21 15:24
 **/
@SpringBootApplication(scanBasePackages = "com.greatwall.jhgx")
public class Application {

    /**
     *  启动入口
     * @param args  参数列表
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
