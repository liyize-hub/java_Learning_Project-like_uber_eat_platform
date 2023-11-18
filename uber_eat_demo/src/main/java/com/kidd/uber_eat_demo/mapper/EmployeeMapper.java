package com.kidd.uber_eat_demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kidd.uber_eat_demo.entity.Employee;

@Mapper
// 继承自mybatis-plus中的basemapper 泛型为Employee
// 常见的crud方法都继承来了
public interface EmployeeMapper extends BaseMapper<Employee> {
}
