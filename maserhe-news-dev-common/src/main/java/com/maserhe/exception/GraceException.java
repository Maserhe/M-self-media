package com.maserhe.exception;

import com.maserhe.grace.result.ResponseStatusEnum;

/**
 * 描述:
 *  优雅的返回异常
 * @author Maserhe
 * @create 2021-05-01 19:58
 */
public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new CustomException(responseStatusEnum);
    }

}
