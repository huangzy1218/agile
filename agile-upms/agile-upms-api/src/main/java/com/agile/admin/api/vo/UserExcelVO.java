package com.agile.admin.api.vo;

import com.agile.plugin.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User export and import.
 *
 * @author Huang Z.Y.
 */
@Data
@ColumnWidth(30)
public class UserExcelVO implements Serializable {

    private static final long serialVersionUID = -1422451080224549876L;

    /**
     * The line number is displayed during the import.
     */
    @ExcelLine
    @ExcelIgnore
    private Long lineNum;

    /**
     * Primary key ID.
     */
    @ExcelProperty("User ID")
    private Long userId;

    /**
     * Username.
     */
    @NotBlank(message = "The user name cannot be empty")
    @ExcelProperty("Username")
    private String username;

    /**
     * Mobile phone number.
     */
    @NotBlank(message = "The phone number cannot be empty")
    @ExcelProperty("Mobile phone number")
    private String phone;

    /**
     * Mobile phone number.
     */
    @NotBlank(message = "Nicknames cannot be empty")
    @ExcelProperty("Nickname")
    private String nickname;

    /**
     * Name.
     */
    @NotBlank(message = "The name cannot be blank")
    @ExcelProperty("Name")
    private String name;

    /**
     * Email.
     */
    @NotBlank(message = "The mailbox cannot be empty")
    @ExcelProperty("Mailbox")
    private String email;

    /**
     * Department name.
     */
    @NotBlank(message = "The department name cannot be empty")
    @ExcelProperty("Department name")
    private String deptName;

    /**
     * Role name list.
     */
    @NotBlank(message = "The role cannot be empty")
    @ExcelProperty("角色")
    private String roleNameList;

    /**
     * Role list.
     */
    @NotBlank(message = "The post must not be empty")
    @ExcelProperty("Job title")
    private String postNameList;

    /**
     * Lock mark.
     */
    @ExcelProperty("Lock mark: 0: normal,9: locked")
    private String lockFlag;

    /**
     * Creation time.
     */
    @ExcelProperty(value = "Creation time")
    private LocalDateTime createTime;

}

    