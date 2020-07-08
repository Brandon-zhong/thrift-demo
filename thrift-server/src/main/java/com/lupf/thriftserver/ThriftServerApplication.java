package com.lupf.thriftserver;

import com.lupf.thriftserver.server.ThriftServer2;
import com.lupf.thriftserver.thriftconfig.ThriftListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ThriftServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThriftServerApplication.class, args).addApplicationListener(new ThriftListener());
    }

    /**
     * 向Spring注册一个Bean对象
     * initMethod = "start"  表示会执行名为start的方法
     * start方法执行之后，就会阻塞接受客户端的请求
     *
     * @return
     */
    @Bean(initMethod = "start")
    public ThriftServer2 init() {
        ThriftServer2 thriftServer = new ThriftServer2();
        return thriftServer;
    }

}
