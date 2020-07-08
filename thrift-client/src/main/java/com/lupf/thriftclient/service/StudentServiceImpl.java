package com.lupf.thriftclient.service;

import com.lupf.thriftapi.Student;
import com.lupf.thriftclient.config.ThriftProvider;
import com.lupf.thriftclient.thrift.pool.TTSocket;
import com.lupf.thriftclient.thrift.pool.ThriftClientConnectPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ThriftProvider
public class StudentServiceImpl implements StudentServiceInf {
    //每次创建一个新的连接的工具类
    //@Autowired
    //ThriftClient thriftClient;

    /**
     * 连接池
     */
    @Autowired
    ThriftClientConnectPoolFactory thriftClientPool;

    @Override
    public Student getStudentByName(String name) {

        TTSocket ttSocket = null;
        try {
            //通过对象池，获取一个服务客户端连接对象
            ttSocket = thriftClientPool.getConnect();
            System.out.println(ttSocket);
            System.out.println("客户端请求用户名为:" + name + "的数据");
            //调用远端对象
            Student student = ttSocket.getService().getStudentByName(name);
            System.out.println("获取成功！！！服务端返回的对象:" + student);
            //用完之后把对象还给对象池
            thriftClientPool.returnConnection(ttSocket);
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常则将当前对象从池子移除
            thriftClientPool.invalidateObject(ttSocket);
        } finally {
        }
        return null;
    }

    @Override
    public void save(Student student) {
        TTSocket ttSocket = null;
        try {
            ttSocket = thriftClientPool.getConnect();
            System.out.println("客户端请求保存对象:" + student);
            ttSocket.getService().save(student);
            System.out.println("保存成功！！！");
            thriftClientPool.returnConnection(ttSocket);
        } catch (Exception e) {
            e.printStackTrace();
            thriftClientPool.returnConnection(ttSocket);
        } finally {
        }
    }
}
