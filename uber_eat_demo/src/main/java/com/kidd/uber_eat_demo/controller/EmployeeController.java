package com.kidd.uber_eat_demo.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kidd.uber_eat_demo.common.R;
import com.kidd.uber_eat_demo.entity.Employee;
import com.kidd.uber_eat_demo.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * employee login
     * 
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    // json形式接收 ➕注解requestBody
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("employee trying to login in...");
        // 1. Encrypt the password submitted from the page using MD5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. Retrieve data from the database based on the username submitted through
        // the web page.
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. If no data is found, return a login failure result.
        if (emp == null) {
            return R.error("login failed");
        }

        // 4. Password verification failed, returning a login failure result if they do
        // not match."
        if (!emp.getPassword().equals(password)) {
            return R.error("the password was not correct, login failed");
        }

        // 5. Check the employee's status; if it is in a disabled state, return a result
        // indicating that the account has been banned.
        if (emp.getStatus() == 0) {
            return R.error("the account has been banned");
        }

        // 6. Upon successful login, store the employee ID in the session and return a
        // login success result.
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * employee logout
     * 
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        log.info("employee trying to log out...");
        // Clear the stored employee ID in the session
        request.getSession().removeAttribute("employee");
        return R.success("logout successfully");
    }

    /**
     * 新增员工
     * 
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());

        // 设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增用户成功");
    }

    /**
     * 员工信息分页查询
     * 
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * 
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }

        return R.error("没有查到对应员工信息");
    }

}
