package com.maserhe.file.controller;

import com.maserhe.api.controller.files.FileUploaderControllerApi;
import com.maserhe.entity.BO.NewAdminBO;
import com.maserhe.file.service.Impl.UploadServiceImpl;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述:
 *     文件服务的上传文件
 * @author Maserhe
 * @create 2021-05-03 12:37
 */
@RestController
public class FilerUploaderController implements FileUploaderControllerApi {

    // http://182.92.10.243:88/maserhe/M00/00/00/rBftpWCPlNeAEyRUAADyXB6avQ8256.jpg
    final static Logger logger = LoggerFactory.getLogger(FilerUploaderController.class);

    @Value("${fastDFS.pathPrefix}")
    private String pathPrefix;

    @Autowired
    private UploadServiceImpl uploadService;

    /**
     * 上传用户头像
     * @param userId
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {

        if (file != null) {
            String fileName = file.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                // http://182.92.10.243:88/maserhe/M00/00/00/rBftpWAddYaABEByAAAn6E9KMW4101.png
                // String[] split = fileName.split("\\.");
                String[] fileNameArr = fileName.split("\\.");
                String suffixName = fileNameArr[fileNameArr.length - 1];
                if (!"png".equalsIgnoreCase(suffixName)
                    && !"jpg".equalsIgnoreCase(suffixName)
                    && !"jpeg".equalsIgnoreCase(suffixName)) {
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                String s = uploadService.uploadFDFS(file, suffixName);
                logger.info("上传头像的路径path === > " + pathPrefix + s);
                return GraceJSONResult.ok(pathPrefix + s);
            }
        }
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
    }

    @Override
    public GraceJSONResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {
        return null;
    }

    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO) throws Exception {
        return null;
    }

    @Override
    public void readInGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
