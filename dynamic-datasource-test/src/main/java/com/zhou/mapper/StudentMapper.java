package com.zhou.mapper;

import com.zhou.model.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou22
 * @since 2025-02-23
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
