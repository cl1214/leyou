package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.conf.UploadProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@EnableConfigurationProperties(UploadProperties.class)
@Service
public class UpLoadService {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties prop;
    public String uploadImage(MultipartFile file) throws IOException {
        List types = Arrays.asList("image/png","image/jpeg");
        //检查文件类型
        String contentType = file.getContentType();
        if(!types.contains(contentType)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        BufferedImage image = ImageIO.read(file.getInputStream());
        if(image == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        String exstion = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
        StorePath storePath =  storageClient.uploadFile(file.getInputStream(),file.getSize(),exstion,null);
        String url = prop.getBaseUrl() + storePath.getFullPath();

        return url;
    }
}
