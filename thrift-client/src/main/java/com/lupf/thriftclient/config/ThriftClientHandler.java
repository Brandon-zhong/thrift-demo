package com.lupf.thriftclient.config;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author brandon
 * create on 2020-07-10
 * desc:
 */
public class ThriftClientHandler implements InvocationHandler {

    private Class<? extends TServiceClient> serviceClientClass;
    private ThriftTransportPooled transportPooled;

    public ThriftClientHandler(Class<? extends TServiceClient> serviceClientClass, ThriftTransportPooled transportPooled) {
        //校验是否实现了相应的Iface接口
        String name = serviceClientClass.getName();
        name = name.substring(0, name.indexOf("$") + 1) + "Iface";
        boolean flag = false;
        for (Class<?> cl : serviceClientClass.getInterfaces()) {
            if (cl.getName().equals(name)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new IllegalArgumentException(serviceClientClass.getName() + " has not implements " + name);
        }
        this.serviceClientClass = serviceClientClass;
        this.transportPooled = transportPooled;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //创建thrift client
        Constructor<? extends TServiceClient> constructor = serviceClientClass.getConstructor(TProtocol.class);
        //从连接池中获取连接
        TTransport transport = transportPooled.getTransport();
        try {
            TServiceClient tServiceClient = constructor.newInstance(new TCompactProtocol(transport));
            return method.invoke(tServiceClient, args);
        } finally {
            //用完归还socket
            if (transport != null) {
                transportPooled.returnTransport(transport);
            }
        }
    }
}
