package com.web.shop.util;

import com.web.shop.bean.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class PathUtil {

    // 获取上传路径
    public static String getUploadPath(String projectPath, String uploadPath) {
        // 项目路径
        File project = new File(projectPath);
        if (!project.exists()) project = new File("");
        // 上传路径
        File upload = new File(project.getAbsolutePath(), "upload" + uploadPath);
        if (!upload.exists()) upload.mkdirs();
        return upload.getPath();
    }

    // 获取文件名后缀
    public static String getSuffixName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
