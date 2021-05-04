package com.maserhe.admin.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maserhe.admin.service.AdminUserService;
import com.maserhe.entity.AdminDo;
import com.maserhe.entity.BO.NewAdminBO;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.mapper.AdminMapper;
import com.maserhe.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 15:51
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminMapper adminMapper;


    @Autowired
    private Sid sid;

    @Override
    public AdminDo queryAdminDoByName(String username) {
        Example example = new Example(AdminDo.class);
        example.createCriteria().andEqualTo("username", username);
        return adminMapper.selectOneByExample(example);
    }

    @Override
    public void createAdminDo(NewAdminBO newAdminBO) {

        //  生成faceId
        String s = sid.nextShort();
        AdminDo adminDo = new AdminDo();
        adminDo.setId(s);
        adminDo.setUsername(newAdminBO.getUsername());
        adminDo.setPassword(newAdminBO.getPassword());

        // 如果密码不为空，则使用密码入库
        if (StringUtils.isNotBlank((newAdminBO.getPassword()))) {
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(),BCrypt.gensalt());
            adminDo.setPassword(pwd);
        }
        // 如果人脸入库，则有FaceId
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())) {
            adminDo.setFaceId(newAdminBO.getFaceId());
        }
        adminDo.setCreatedTime(new Date());
        adminDo.setUpdatedTime(new Date());

        // 插入数据库
        int insert = adminMapper.insert(adminDo);
        if (insert != 1) {
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }


    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example example = new Example(AdminDo.class);
        example.orderBy("createdTime").desc();
        PageHelper.startPage(page, pageSize);
        List<AdminDo> adminDOList = adminMapper.selectByExample(example);
        // System.out.println(adminDOList);
        return setterPagedGrid(adminDOList, page);
    }

    private PagedGridResult setterPagedGrid(List<?> adminUserList, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(adminUserList);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRecords(pageList.getPages());
        result.setTotal(pageList.getTotal());
        result.setRows(pageList.getList());
        return result;
    }
}
