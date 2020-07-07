package com.lupf.thriftclient.pooltest;

public class PoolFactoryTest {
    public static void main(String[] args) throws Exception {
        PoolFactory poolFactory = new PoolFactory();

        PoolObj poolObj = poolFactory.getObj();
        System.out.println(poolObj);

        poolFactory.invalidateObject(poolObj);

        PoolObj poolObj1 = poolFactory.getObj();
        System.out.println(poolObj1);
    }

    public static class MyRunable implements Runnable {
        PoolFactory poolFactory;
        PoolObj obj;

        public MyRunable(PoolFactory poolFactory) {
            try {
                this.poolFactory = poolFactory;
                this.obj = poolFactory.getObj();
                System.out.println(this.obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                obj.busi();
                poolFactory.returnObj(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
