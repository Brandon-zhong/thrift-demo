package com.lupf.thriftclient.pooltest;

import java.util.Random;

public class PoolObj {

    public void busi() {
        System.out.println("busi start");
        try {
            Random random = new Random();

            int num = random.nextInt(10) + 1;
            Thread.sleep(num * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("busi end");
    }
}
