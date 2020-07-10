package com.lupf.thriftclient.config;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author brandon
 * create on 2020-07-09
 * desc: thrift transport创建工厂
 */
public class ThriftTransportPoolFactory implements PoolableObjectFactory<TTransport> {

    private String host;
    private int port;
    private int timeout;

    public ThriftTransportPoolFactory(String host, int port, int timeout) {
        super();
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public TTransport makeObject() throws Exception {
        TTransport tTransport = new TFramedTransport(new TSocket(host, port, timeout));
        tTransport.open();
        return tTransport;
    }

    @Override
    public void destroyObject(TTransport tTransport) throws Exception {
        if (tTransport.isOpen()) {
            tTransport.close();
        }
    }

    @Override
    public boolean validateObject(TTransport tTransport) {
        if (!(tTransport instanceof TSocket)) {
            return false;
        }
        return tTransport.isOpen();
    }

    @Override
    public void activateObject(TTransport tTransport) throws Exception {

    }

    @Override
    public void passivateObject(TTransport tTransport) throws Exception {

    }
}
