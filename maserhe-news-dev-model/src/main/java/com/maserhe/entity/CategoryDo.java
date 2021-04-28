package com.maserhe.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "category")
public class CategoryDo {

    @Id
    private Integer id;

    private String name;

    private String tagColor;

    public CategoryDo(Integer id, String name, String tagColor) {
        this.id = id;
        this.name = name;
        this.tagColor = tagColor;
    }

    public CategoryDo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor == null ? null : tagColor.trim();
    }
}