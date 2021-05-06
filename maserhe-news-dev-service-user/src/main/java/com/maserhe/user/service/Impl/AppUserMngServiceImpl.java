package com.maserhe.user.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maserhe.entity.UserDo;
import com.maserhe.enums.UserStatus;
import com.maserhe.mapper.UserMapper;
import com.maserhe.user.service.AppUserMngService;
import com.maserhe.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 17:38
 */
@Service
public class AppUserMngServiceImpl implements AppUserMngService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PagedGridResult queryAllUserList(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(UserDo.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        // 模糊查询
        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        // 状态查询
        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus", status);
        }
        // 时间查询
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }

        // 分页
        PageHelper.startPage(page, pageSize);
        List<UserDo> list = userMapper.selectByExample(example);
        return setterPagedGrid(list, page);
    }

    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        UserDo user = new UserDo();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        userMapper.updateByPrimaryKeySelective(user);
    }
    /**
     * 分页使用
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setterPagedGrid(List<?> list,
                                           Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}
