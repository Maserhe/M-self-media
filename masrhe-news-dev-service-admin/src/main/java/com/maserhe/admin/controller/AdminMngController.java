package com.maserhe.admin.controller;

import com.maserhe.admin.service.AdminUserService;
import com.maserhe.api.BaseController;
import com.maserhe.api.controller.admin.AdminMngControllerApi;
import com.maserhe.entity.AdminDo;
import com.maserhe.entity.BO.AdminLoginBO;
import com.maserhe.entity.BO.NewAdminBO;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.PagedGridResult;
import com.maserhe.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 描述:
 *  管理源接口的具体实现
 * @author Maserhe
 * @create 2021-05-03 15:55
 */
@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    @Autowired
    private AdminUserService adminService;

    @Autowired
    private RedisOperator redisOperator;


    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {

        // 1, 验证用户名和密码是否为空
        if (StringUtils.isBlank(adminLoginBO.getUsername()) ) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        if (StringUtils.isBlank(adminLoginBO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }
        // 查询用户
        AdminDo admin = adminService.queryAdminDoByName(adminLoginBO.getUsername());
        if (admin == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        // 2, 检查用户名和密码
        boolean isPWDMatch = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
        if (isPWDMatch) {
            // 保存用户会话信息
            doLoginSetting(admin, request, response);
            return GraceJSONResult.ok();
        }
        return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
    }

    /**
     * 判断管理员是否存在
     * @param username
     * @return
     */
    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminEXist(username);
        return GraceJSONResult.ok();
    }



    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO, HttpServletRequest request, HttpServletResponse response) {

        // 0, 验证用户名和密码是否为空
        if (StringUtils.isBlank(newAdminBO.getUsername()) ) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }

        // 1， base64不为空则代表是 人脸入库
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) || StringUtils.isBlank(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
            else if (!newAdminBO.getPassword().equals(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        // 2, 验证是否存在
        checkAdminEXist(newAdminBO.getUsername());
        // 3, 存入数据库
        adminService.createAdminDo(newAdminBO);

        return GraceJSONResult.ok();
    }

    /**
     * 分页 第几页， 这一页的大小
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
        // 查询
        PagedGridResult result = adminService.queryAdminList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 删除redis 以及cookie
        redisOperator.del(REDIS_ADMIN_TOKEN + ":" + adminId);
        String encode = URLEncoder.encode("", "utf-8");
        setCookie(request, response, "atoken", encode, 0);
        setCookie(request, response, "aid", encode, 0);
        setCookie(request, response, "aname", encode, 0);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminFaceLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * 用户配置 admin 登陆后的基本信息配置, 会话缓存
     * @param admin
     * @param request
     * @param response
     */
    private void doLoginSetting(AdminDo admin, HttpServletRequest request, HttpServletResponse response) {
        // REDIS_ADMIN_TOKEN
        String token = UUID.randomUUID().toString();
        redisOperator.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(), token);
        // 保存基本信息到 cookie中
        setCookie(request, response, "atoken", token, COOKIE_MONTH);
        setCookie(request, response, "aid", admin.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", admin.getAdminName(), COOKIE_MONTH);
    }

    private void checkAdminEXist(String username) {
        AdminDo adminDo = adminService.queryAdminDoByName(username);
        if (adminDo != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }


}
