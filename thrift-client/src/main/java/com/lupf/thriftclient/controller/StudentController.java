package com.lupf.thriftclient.controller;

import com.lupf.thriftapi.Student;
import com.lupf.thriftapi.StudentService;
import com.lupf.thriftclient.config.ThriftConfig;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("thrift")
public class StudentController {
    @Resource(name = "studentServiceIface")
//    @Qualifier("studentServiceIface")
    private StudentService.Iface studentServiceIface;

    @Resource
    private ThriftConfig thriftConfig;

    @GetMapping("get")
    public Student getStudeByName(String name) throws TException {
//        studentServiceIface = (StudentService.Iface) thriftConfig.getApplicationContext().getBean("studentServiceIface");
        Student zhong = studentServiceIface.getStudentByName("zhong");
        System.out.println("StudentController.getStudeByName --> " + zhong.toString());
        return zhong;
    }

    @GetMapping("save")
    public Student save() throws TException {
        //直接模拟前端传递的数据
        Student student = new Student();
        student.setName("AAA");
        student.setAge(10);
        student.setAddress("BBB");
        //调用保存服务
        studentServiceIface.save(student);
        return student;
    }
}
