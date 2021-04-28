package com.maserhe.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:39
 */
public interface HelloControllerApi {

    /**
     *  Api 得作用
     *  api 就相当于企业得领导， 老板， 部门经理
     *  所有得 api都是在这里调用的
     */

    @GetMapping("/hello")
    String hello();


}
