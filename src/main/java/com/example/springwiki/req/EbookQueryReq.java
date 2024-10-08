package com.example.springwiki.req;

public class EbookQueryReq extends PageReq {
    private Long id;
    private Long categoryId2;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EbookQueryReq{" +
                "categoryId2=" + categoryId2 +
                ", id=" + id +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

}