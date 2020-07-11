package com.lupf.thriftserver.server;

import com.lupf.thriftapi.DataException;
import com.lupf.thriftapi.Student;
import com.lupf.thriftapi.StudentService;
import com.lupf.thriftserver.thriftconfig.ThriftProvider;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * 服务端具体的操作的实现
 */
@Service
@ThriftProvider
public class MyServerServiceImpl implements StudentService.Iface {
    @Override
    public Student getStudentByName(String name) throws DataException, TException {
        System.out.println("服务端收到客户端获取用户名:" + name + "信息");
        Student student = new Student();
        student.setName(name);
        student.setAge(100);
        student.setAddress("深圳");

        //模拟耗时
        System.out.println("模拟耗时...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("模拟获取成功并返回：" + student);
        return student;
    }

    @Override
    public void save(Student student) throws DataException, TException {
        System.out.println("服务端收到客户端请求保存学生信息：" + student);
        //模拟耗时
        System.out.println("模拟耗时...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("模拟保存成功!!!");
    }
}
