package com.example.springwiki.resp;

import javax.validation.constraints.NotNull;

public class CategoryQueryResp {
    private Long id;

    private Long parent;
    @NotNull(message = "[名称]不能为空")
    private String name;
    @NotNull(message = "[排序]不能为空")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", parent=" + parent +
                ", name=" + name +
                ", sort=" + sort +
                "]";
        return sb;
    }
}