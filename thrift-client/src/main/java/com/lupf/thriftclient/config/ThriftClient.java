package com.lupf.thriftclient.config;

import org.apache.thrift.transport.TTransportException;

/**
 * @author brandon
 * create on 2020-07-09
 * desc: 定义客户端开关行为的接口
 */
public interface ThriftClient {

    void open() throws TTransportException;

    void close();

}
