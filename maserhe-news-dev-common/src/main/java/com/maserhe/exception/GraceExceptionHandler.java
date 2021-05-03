package com.maserhe.exception;

import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    /**
     * 上传文件 文件大小超出异常。
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public GraceJSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }


}
