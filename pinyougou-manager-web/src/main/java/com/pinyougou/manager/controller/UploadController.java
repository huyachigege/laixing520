package com.pinyougou.manager.controller;

import com.pinyougou.entity.CRUDResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;
    @RequestMapping("upload")
    public CRUDResult upload(MultipartFile file) {
        try {
            FastDFSClient client =new FastDFSClient("classpath:config/fdfs_client.conf");
            String name = file.getOriginalFilename();
            String extName = name.substring(name.lastIndexOf(".") + 1);
            String fileId = client.uploadFile(file.getBytes(), extName);
            String url = FILE_SERVER_URL + fileId;
            return new CRUDResult(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new CRUDResult(false,"上传失败" );
        }

    }
}
