package com.example.springwiki.req;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserLoginReq {

    @NotNull(message = "[用户名]不能为空")
    private String loginName;

    @NotNull(message = "[密码]不能为空")
    @Length(min = 6, max = 32, message = "[密码] 不正确")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "[密码]规则不正确")
    private String password;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", loginName=" + loginName +
                ", password=" + password +
                "]";
        return sb;
    }
}