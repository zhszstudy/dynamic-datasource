package com.zhou.service.impl;

import com.zhou.annotation.DataSourceType;
import com.zhou.model.Student;
import com.zhou.mapper.StudentMapper;
import com.zhou.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhou22
 * @since 2025-02-23
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public List<Student> queryStudentList() {
        return baseMapper.selectList(null);
    }

    @DataSourceType(dataSourceName = "slave")
//    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Student> queryStudentFromSlave() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Student> queryStudentWithSelf() {
        StudentService studentService = (StudentService) AopContext.currentProxy();
        return studentService.queryStudentFromSlave();
    }

    @Override
    public void saveStudent(Student student) {
        baseMapper.insert(student);
    }
}
