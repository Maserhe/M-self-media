package com.maserhe.file.service.Impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.maserhe.file.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 12:29
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FastFileStorageClient client;

    @Override
    public String uploadFDFS(MultipartFile file, String fileExtName) throws IOException {
        StorePath storePath = client.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
        return storePath.getFullPath();
    }

}
