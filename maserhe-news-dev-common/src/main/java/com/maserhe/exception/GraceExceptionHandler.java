package com.maserhe.exception;

import com.maserhe.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述:
 *  统一异常处理, 对所有的Controller进行异常捕获
 * @author Maserhe
 * @create 2021-05-01 20:11
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public GraceJSONResult returnException(CustomException e) {
        // 对自定义异常进行处理
        // e.printStackTrace(); // 打印
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }

}
