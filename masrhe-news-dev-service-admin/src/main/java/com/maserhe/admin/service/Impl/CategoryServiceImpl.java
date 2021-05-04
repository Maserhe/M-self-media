package com.maserhe.admin.service.Impl;

import com.maserhe.admin.service.CategoryService;
import com.maserhe.entity.CategoryDo;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.mapper.CategoryMapper;
import com.maserhe.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import static com.maserhe.api.BaseController.REDIS_ALL_CATEGORY;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 16:46
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisOperator redisOperator;


    @Override
    public void createCategory(CategoryDo category) {
        int result = categoryMapper.insert(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

    }

    @Override
    @Transactional
    public void modifyCategory(CategoryDo category) {
        categoryMapper.updateByPrimaryKey(category);
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        /**
         * 不建议如下做法：
         * 1. 查询redis中的categoryList
         * 2. 循环categoryList中拿到原来的老的数据
         * 3. 替换老的category为新的
         * 4. 再次转换categoryList为json，并存入redis中
         */
        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redisOperator.del(REDIS_ALL_CATEGORY);
    }

    /**
     * 这里是跟新操作，新的名字和老的名字
     * @param catName
     * @param oldCatName
     * @return
     */
    @Override
    public boolean queryCatIsExist(String catName, String oldCatName) {
        Example example = new Example(CategoryDo.class);
        Example.Criteria catCriteria = example.createCriteria();
        catCriteria.andEqualTo("name", catName);
        if (StringUtils.isNotBlank(oldCatName)) {
            catCriteria.andNotEqualTo("name", oldCatName);
        }

        List<CategoryDo> catList = categoryMapper.selectByExample(example);
        boolean isExist = false;
        if (catList != null && !catList.isEmpty() && catList.size() > 0) {
            isExist = true;
        }
        return isExist;
    }

    @Override
    public List<CategoryDo> queryCategoryList() {
        return categoryMapper.selectAll();
    }
}
