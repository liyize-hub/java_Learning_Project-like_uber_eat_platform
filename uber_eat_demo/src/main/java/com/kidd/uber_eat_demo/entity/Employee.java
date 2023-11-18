package com.kidd.uber_eat_demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * Employee entity
 */
// 用于标记类的实例可以被序列化为字节流，以便将对象保存到文件、传输到网络或在进程之间进行通信。
// 实现 Serializable 接口的类可以被 Java 序列化机制处理，从而可以将对象转换为字节流或从字节流恢复对象。
// 为了确保反序列化时类的版本兼容性，可以使用 serialVersionUID 字段来控制类的版本。
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber; // 身份证号码

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // MyBatis-Plus 框架中的注解，用于指定实体类中的字段在插入操作（INSERT）时应该如何填充数据
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    // FieldFill.INSERT_UPDATE：表示在插入和更新数据时都进行填充，通常用于设置创建时间和更新时间等数据。
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
