package com.lupf.thriftserver.thriftconfig;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author brandon
 * create on 2020-07-08
 * desc:
 */
public class ThriftListener implements ApplicationListener<ApplicationEvent> {

    public ThriftListener() {
        System.out.println("ThriftListener.ThriftListener");
    }

    /*@Override
    public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
        if (contextStartedEvent.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = contextStartedEvent.getApplicationContext().getBeansWithAnnotation(ThriftProvider.class);
            for (Map.Entry<String, Object> entry : beans.entrySet()) {
                System.out.println(entry.getKey() + " --> " + entry.getValue().getClass().getName());
            }
        }
        System.out.println("ThriftConfig.onApplicationEvent --> " + contextStartedEvent.getSource().getClass().getName());
    }*/

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        System.out.println("ThriftListener.onApplicationEvent --> " + applicationEvent.getSource().getClass().getName());
    }
}
