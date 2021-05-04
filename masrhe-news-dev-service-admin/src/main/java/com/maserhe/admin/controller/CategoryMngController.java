package com.maserhe.admin.controller;

import com.maserhe.admin.service.CategoryService;
import com.maserhe.admin.service.Impl.CategoryServiceImpl;
import com.maserhe.api.controller.admin.CategoryMngControllerApi;
import com.maserhe.entity.BO.SaveCategoryBO;
import com.maserhe.entity.CategoryDo;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.maserhe.api.BaseController.REDIS_ALL_CATEGORY;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 16:41
 */
@RestController
public class CategoryMngController implements CategoryMngControllerApi {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private RedisOperator redisOperator;


    @Override
    public GraceJSONResult saveOrUpdateCategory(@Valid SaveCategoryBO saveCategoryBO) {

        CategoryDo newCat = new CategoryDo();
        BeanUtils.copyProperties(saveCategoryBO, newCat);
        // id为空新增，不为空修改
        if (saveCategoryBO.getId() == null) {
            // 查询新增的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(newCat.getName(), null);
            if (!isExist) {
                // 新增到数据库
                categoryService.createCategory(newCat);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        } else {
            // 查询修改的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(newCat.getName(), saveCategoryBO.getOldName());
            if (!isExist) {
                // 修改到数据库
                categoryService.modifyCategory(newCat);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {
        List<CategoryDo> categoryList = categoryService.queryCategoryList();
        return GraceJSONResult.ok(categoryList);
    }

    @Override
    public GraceJSONResult getCats() {
        // 先从redis中查询，如果有，则返回，如果没有，则查询数据库库后先放缓存，放返回
        String allCatJson = redisOperator.get(REDIS_ALL_CATEGORY);

        List<CategoryDo> categoryList = null;
        if (StringUtils.isBlank(allCatJson)) {
            categoryList = categoryService.queryCategoryList();
            redisOperator.set(REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(allCatJson, CategoryDo.class);
        }
        return GraceJSONResult.ok(categoryList);
    }
}
