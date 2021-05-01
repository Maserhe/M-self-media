package com.maserhe.exception;

import com.maserhe.grace.result.ResponseStatusEnum;

/**
 * 描述:
 *  自定义统一的异常类
 * @author Maserhe
 * @create 2021-05-01 20:00
 */

public class CustomException extends RuntimeException {

    ResponseStatusEnum responseStatusEnum;

    public CustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码:" + responseStatusEnum.status() + ";具体异常信息为:" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
