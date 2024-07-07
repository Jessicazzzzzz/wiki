package com.example.springwiki.util;


import com.example.springwiki.resp.UserLoginResp;

import java.io.Serializable;

public class LoginUserContext implements Serializable {

    private static final ThreadLocal<UserLoginResp> user = new ThreadLocal<>();

    public static UserLoginResp getUser() {
        return user.get();
    }

    public static void setUser(UserLoginResp user) {
        LoginUserContext.user.set(user);
    }

}


