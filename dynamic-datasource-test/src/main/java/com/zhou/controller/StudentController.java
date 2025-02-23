package com.zhou.controller;

import com.zhou.model.Student;
import com.zhou.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhou22
 * @@description 控制层
 * @date 2025/2/23
 **/
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("/list")
    public List<Student> queryStudentList(){
        return studentService.queryStudentList();
    }

    @GetMapping("/listFromSlave")
    public List<Student> queryStudentFromSlave(){
        return studentService.queryStudentFromSlave();
    }

    @GetMapping("/listWithSelf")
    public List<Student> queryStudentWithSelf(){
        return studentService.queryStudentWithSelf();
    }

    @PostMapping("/save")
    public String saveStudent(Student student){
        studentService.saveStudent(student);
        return "ok";
    }

}
