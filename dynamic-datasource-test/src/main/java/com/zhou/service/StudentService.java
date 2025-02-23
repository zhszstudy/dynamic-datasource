package com.zhou.service;

import com.zhou.model.Student;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhou22
 * @since 2025-02-23
 */
public interface StudentService extends IService<Student> {

    List<Student> queryStudentList();

    List<Student> queryStudentFromSlave();

    List<Student> queryStudentWithSelf();

    void saveStudent(Student student);

}
