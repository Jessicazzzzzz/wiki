package com.example.springwiki.req;

/**
 * @author jessica~
 * @version 1.0
 */
public class UserQueryReq extends PageReq {
    private String loginName;

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "loginName='" + loginName + '\'' +
                "} " + super.toString();
    }
}
