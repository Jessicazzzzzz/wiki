package com.example.springwiki.resp;

import java.util.List;

public class PageResp<T> {
    private long total;
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        String sb = "PageResp{" + "total=" + total +
                ", list=" + list +
                '}';
        return sb;
    }
}