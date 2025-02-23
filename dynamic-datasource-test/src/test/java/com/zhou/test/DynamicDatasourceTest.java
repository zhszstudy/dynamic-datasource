package com.zhou.test;

import com.zhou.model.Student;
import com.zhou.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author zhou22
 * @@description 测试类
 * @date 2025/2/23
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class DynamicDatasourceTest {

    @Resource
    private StudentService studentService;


    @Test
    public void saveStudentTest() {
        Student student = Student.builder()
                .gender(true)
                .age(20)
                .name("张三")
                .build();
        studentService.saveStudent(student);
    }

}
