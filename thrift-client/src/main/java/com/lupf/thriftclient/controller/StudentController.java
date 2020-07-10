package com.lupf.thriftclient.controller;

import com.iyingdi.user.thrift.UserThriftService;
import com.lupf.thriftapi.Student;
import com.lupf.thriftclient.config.ThriftConfig;
import com.lupf.thriftclient.service.StudentServiceInf;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("thrift")
public class StudentController {
    @Autowired
    StudentServiceInf studentService;

    @Resource
    private UserThriftService.Iface userServiceIface;
    @Resource
    private ThriftConfig thriftConfig;

    @GetMapping("get")
    public Student getStudeByName(String name) throws TException {
        Object bean = thriftConfig.getApplicationContext().getBean("UserThriftService123");
        System.out.println("StudentController.getStudeByName");
        return studentService.getStudentByName(name);
    }

    @GetMapping("save")
    public Student save() {
        //直接模拟前端传递的数据
        Student student = new Student();
        student.setName("AAA");
        student.setAge(10);
        student.setAddress("BBB");
        //调用保存服务
        studentService.save(student);
        return student;
    }
}
