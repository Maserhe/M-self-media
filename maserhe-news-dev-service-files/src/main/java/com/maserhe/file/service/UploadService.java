package com.maserhe.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 12:27
 */
public interface UploadService {

    public String uploadFDFS(MultipartFile file, String fileExtName) throws IOException;

}
