package com.example.springwiki.util;

import java.io.Serializable;

/**
 * @author jessica~
 * @version 1.0
 */
public class RequestContext implements Serializable {
    private static final ThreadLocal<String> remoteAddress = new ThreadLocal<>();

    public static void setRemoteAddress(String remoteAddress) {
        RequestContext.remoteAddress.set(remoteAddress);
    }

    public static String getRemoteAddress() {
        return remoteAddress.get();
    }
}
