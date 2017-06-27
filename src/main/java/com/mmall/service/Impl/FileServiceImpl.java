package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by guojianzhong on 2017/6/27.
 */
@Service("iFileServiceImpl")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();

        String fileExtensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("开始上传文件，上传文件的文件名:{},上传路径:{},新文件名:{}",originalFilename,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }


        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //此时文件生成完毕
            ArrayList<File> files = Lists.newArrayList(targetFile);
            FTPUtil.uploadFile(files);

            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常" + e);
            return null;
        }
        return targetFile.getName();

    }
}


