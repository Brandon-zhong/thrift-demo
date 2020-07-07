package com.lupf.thriftclient.pooltest;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class PoolFactory {

    public GenericObjectPool pool;

    public PoolFactory() {
        BusiFactory busiFactory = new BusiFactory();
        this.pool = new GenericObjectPool(busiFactory, new GenericObjectPool.Config());
    }

    public void returnObj(PoolObj poolObj) throws Exception {
        this.pool.returnObject(poolObj);
    }

    public void invalidateObject(PoolObj poolObj) throws Exception {
        this.pool.invalidateObject(poolObj);
    }

    public PoolObj getObj() throws Exception {
        return (PoolObj) pool.borrowObject();
    }

    public class BusiFactory extends BasePoolableObjectFactory {

        @Override
        public Object makeObject() throws Exception {
            return new PoolObj();
        }
    }
}
