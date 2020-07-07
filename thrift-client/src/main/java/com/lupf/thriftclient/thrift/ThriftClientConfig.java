package com.lupf.thriftclient.thrift;

import com.lupf.thriftclient.thrift.pool.ThriftClientConnectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class ThriftClientConfig {
    //服务端的地址
    @Value("${server.thrift.host}")
    private String host;

    //服务端的端口
    @Value("${server.thrift.port}")
    private Integer port;

    //初始化Bean的时候调用对象里面的init方法
    @Bean(initMethod = "init")
    //每次请求实例化一个新的ThriftClient连接对象
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ThriftClient init() {
        ThriftClient thriftClient = new ThriftClient();
        thriftClient.setHost(host);
        thriftClient.setPort(port);
        return thriftClient;
    }

    @Bean
    public ThriftClientConnectPoolFactory ThriftClientPool() {
        //对象池的配置对象
        //这里测试就直接使用默认的配置
        //可以通过config 设置对应的参数
        //参数说明见  http://commons.apache.org/proper/commons-pool/api-1.6/org/apache/commons/pool/impl/GenericObjectPool.html
        //可以将配置项添加在yaml文件中，这里通过@Value注解获取
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        //创建一个池工厂对象
        ThriftClientConnectPoolFactory thriftClientConnectPoolFactory = new ThriftClientConnectPoolFactory(config, host, port);
        //交由Spring管理
        return thriftClientConnectPoolFactory;
    }
}
