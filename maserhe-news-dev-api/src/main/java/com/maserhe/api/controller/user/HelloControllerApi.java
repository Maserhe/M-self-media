package com.maserhe.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:39
 */
@Api(value = "controlle的标题", tags = {"标签", "标签二"})
public interface HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloControllerApi.class);

    /**
     *  Api 得作用
     *  api 就相当于企业得领导， 老板， 部门经理
     *  所有得 api都是在这里调用的
     */
    @ApiOperation(value = "不知道", notes = "返回hello", httpMethod = "GET")
    @GetMapping("/hello")
    String hello();

}
