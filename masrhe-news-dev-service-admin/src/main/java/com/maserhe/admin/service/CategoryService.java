package com.maserhe.admin.service;

import com.maserhe.entity.CategoryDo;

import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 16:44
 */
public interface CategoryService {

    /**
     * 新增文章分类
     */
    public void createCategory(CategoryDo category);

    /**
     * 修改文章分类列表
     */
    public void modifyCategory(CategoryDo category);

    /**
     * 查询分类名是否已经存在
     */
    public boolean queryCatIsExist(String catName, String oldCatName);

    /**
     * 获得文章分类列表
     */
    public List<CategoryDo> queryCategoryList();
}
