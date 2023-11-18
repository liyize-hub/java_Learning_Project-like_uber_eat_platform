package com.kidd.uber_eat_demo.common;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 */
// 思想：面向切面编程---对原有的代码进行增强 并且不会改变原有的代码
// 第一个注解以及值：
// 所有的调用一般都是从Controller开始的，因此异常最后会抛到Controller ，只需要接受Controller异常即可
@ControllerAdvice(annotations = { RestController.class, Controller.class
})
// 异常处理后，需要返回信息给前端，将信息返回到 响应体中
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * 处理sql相关异常
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }
}
