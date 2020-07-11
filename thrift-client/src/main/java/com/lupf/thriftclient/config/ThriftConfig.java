package com.lupf.thriftclient.config;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TServiceClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author brandon
 * create on 2020-07-09
 * desc:
 */
@Configuration
@ConfigurationProperties(prefix = "thrift.server")
public class ThriftConfig implements DisposableBean, ApplicationContextAware {

    private List<ThriftServerConfigBean> serverList;

    public List<ThriftServerConfigBean> getServerList() {
        return serverList;
    }

    public void setServerList(List<ThriftServerConfigBean> serverList) {
        this.serverList = serverList;
        init();
    }

    private Map<String, ThriftTransportPooled> transportManagerMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void init() {
        System.out.println("ThriftConfig.init");
        if (serverList == null) {
            return;
        }
        for (ThriftServerConfigBean bean : serverList) {
            if (transportManagerMap.containsKey(bean.getAppName())) {
                continue;
            }
            GenericObjectPool.Config config = new GenericObjectPool.Config();
            config.minIdle = 300;
            config.maxIdle = 400;
            config.maxActive = config.maxIdle;
            config.maxWait = 30000;
            config.lifo = false;
            ThriftTransportPooled transportPooled = new ThriftTransportPooled(bean.getHost(), bean.getPort(), config, bean);
            transportManagerMap.put(bean.getAppName(), transportPooled);
            //创建bean
            ClassLoader classLoader = getClass().getClassLoader();
            for (String serviceName : bean.getInterfaceList()) {
                try {
                    Class<?> aClass = classLoader.loadClass(serviceName);
                    registerBean(aClass, transportPooled);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerBean(Class<?> serviceClass, ThriftTransportPooled transportPooled) throws ClassNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        //加载service Iface接口
        Class<?> serviceIfaceClass = classLoader.loadClass(serviceClass.getName() + "$Iface");
//        Object serviceProxy = createServiceProxy(serviceClass, serviceIfaceClass, transportPooled, classLoader);
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(serviceClass, () -> {
            try {
                return createServiceProxy(serviceClass, serviceIfaceClass, transportPooled, classLoader);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });
        // 注册bean
        String beanName = serviceClass.getSimpleName() + "Iface";
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
        System.out.println("ThriftConfig.demo --> " + beanName);
    }

    @SuppressWarnings("unchecked")
    private <T> T createServiceProxy(Class<?> serviceClass, Class<?> serviceIfaceClass, ThriftTransportPooled transportPooled, ClassLoader classLoader) throws ClassNotFoundException {
        //加载继承了TServiceClient和实现了Iface接口的Client类
        Class<? extends TServiceClient> clientClass = (Class<? extends TServiceClient>) classLoader.loadClass(serviceClass.getName() + "$Client");
        //创建invokeHandler
        ThriftClientHandler thriftClientHandler = new ThriftClientHandler(clientClass, transportPooled);
        //创建对应的client实现类
        T object = (T) Proxy.newProxyInstance(classLoader, new Class[]{serviceIfaceClass}, thriftClientHandler);
        return object;
    }


    //关闭thrift连接池
    @Override
    public void destroy() throws Exception {
        if (transportManagerMap == null) {
            return;
        }
        for (ThriftTransportPooled pooled : transportManagerMap.values()) {
            try {
                pooled.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
