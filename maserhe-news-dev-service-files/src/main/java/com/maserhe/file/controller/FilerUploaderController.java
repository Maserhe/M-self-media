package com.maserhe.file.controller;

import com.maserhe.api.controller.files.FileUploaderControllerApi;
import com.maserhe.entity.BO.NewAdminBO;
import com.maserhe.exception.GraceException;
import com.maserhe.file.service.Impl.UploadServiceImpl;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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


    @Autowired
    private GridFSBucket gridFSBucket;


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

    /**
     * 多文件上传
     * @param userId
     * @param files
     * @return
     * @throws Exception
     */
    @Override
    public GraceJSONResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {

        // 声明一个list 用于存放多个土拍你恶的路径地址
        List<String> list = new ArrayList<>();

        if (files != null && files.length > 0) {
            // 循环处理
            for (MultipartFile file: files) {
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
                            continue; // 处理下一个图片
                        }
                        String s = uploadService.uploadFDFS(file, suffixName);
                        logger.info("上传头像的路径path === > " + pathPrefix + s);
                        list.add(pathPrefix + s);
                    }
                }
            }
        }
        return GraceJSONResult.ok(list);
    }

    /**
     * 文件上传到 gridFS
     * @param newAdminBO
     * @return
     * @throws Exception
     */
    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO) throws Exception {

        // 获取图片的base64
        String file64 = newAdminBO.getImg64();
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());
        // 转换为输入流
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);

        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", is);
        // 获取文件gridfs中的主键
        String fileIdStr = fileId.toString();
        return GraceJSONResult.ok(fileIdStr);
    }

    /**
     * 输出人脸信息到浏览器
     * @param faceId
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void readInGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 0. 判断参数
        if (StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 1. 从gridfs中读取
        File adminFace = readGridFSByFaceId(faceId);

        // 2. 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response, adminFace);
    }

    @Override
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 0. 获得gridfs中人脸文件
        File myface = readGridFSByFaceId(faceId);

        // 1. 转换人脸为base64
        String base64Face = FileUtils.fileToBase64(myface);

        return GraceJSONResult.ok(base64Face);
    }


    // 读取face 信息
    private File readGridFSByFaceId(String faceId) throws Exception {

        GridFSFindIterable gridFSFiles
                = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));

        GridFSFile gridFS = gridFSFiles.first();

        if (gridFS == null) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        String fileName = gridFS.getFilename();
        System.out.println(fileName);

        // 获取文件流，保存文件到本地或者服务器的临时目录
        File fileTemp = new File("/workspace/temp_face");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

        File myFile = new File("/workspace/temp_face/" + fileName);

        // 创建文件输出流
        OutputStream os = new FileOutputStream(myFile);
        // 下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId), os);

        return myFile;
    }
}
