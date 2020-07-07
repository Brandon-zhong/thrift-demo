package com.lupf.thriftclient.service;


import com.lupf.thriftapi.Student;

public interface StudentServiceInf {
    //根据名称获取学生信息
    Student getStudentByName(String name);

    //保存学生信息
    void save(Student student);
}
