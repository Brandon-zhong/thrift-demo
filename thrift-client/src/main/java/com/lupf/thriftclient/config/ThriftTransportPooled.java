package com.lupf.thriftclient.config;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.transport.TTransport;

/**
 * @author brandon
 * create on 2020-07-09
 * desc:
 */
public class ThriftTransportPooled {

    private String host;
    private int port;
    private int timeout;
    private GenericObjectPool.Config config;
    private ObjectPool<TTransport> transportObjectPool;
    private ThriftServerConfigBean serverConfigBean;

    public ThriftTransportPooled(String host, int port, GenericObjectPool.Config config, ThriftServerConfigBean serverConfigBean) {
        this.host = host;
        this.port = port;
        this.timeout = 0;
        this.config = config;
        transportObjectPool = new GenericObjectPool<>(new ThriftTransportPoolFactory(host, port, timeout), config);
        this.serverConfigBean = serverConfigBean;
    }

    public TTransport getTransport() throws Exception {
        return transportObjectPool.borrowObject();
    }

    public void returnTransport(TTransport transport) throws Exception {
        transportObjectPool.returnObject(transport);
    }

    public void destroy() throws Exception {
        transportObjectPool.close();
    }


}
