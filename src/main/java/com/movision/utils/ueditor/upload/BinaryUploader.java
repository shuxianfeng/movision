package com.movision.utils.ueditor.upload;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.oss.AliOSSClient;
import com.movision.utils.ueditor.PathFormat;
import com.movision.utils.ueditor.define.AppInfo;
import com.movision.utils.ueditor.define.BaseState;
import com.movision.utils.ueditor.define.FileType;
import com.movision.utils.ueditor.define.State;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class BinaryUploader {


    public static final State saveToObject(HttpServletRequest request, Map<String, Object> conf) {
        String uploadMode = PropertiesLoader.getValue("upload.mode");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile upfile = multipartRequest.getFile("upfile");
        if ("zhb".equals(uploadMode)) {
            return save(conf, upfile);
        } else if ("alioss".equals(uploadMode)) {
            return saveToAliOSS(conf, upfile);
        } else {
            return new BaseState(false);
        }
    }


    public static final State saveToAliOSS(Map<String, Object> conf, MultipartFile upfile) {
        String originFileName = upfile.getOriginalFilename();
        String suffix = FileType.getSuffixByFilename(originFileName);

        originFileName = originFileName.substring(0,
                originFileName.length() - suffix.length());

        long maxSize = (Long) conf.get("maxSize");

        if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
            return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
        }

        AliOSSClient aliOSSClient = new AliOSSClient();
        String type = (String) conf.get("actionType");

        Map<String, String> result = aliOSSClient.uploadStream(upfile, maxSize,
                type.equals("img") ? "img" : "doc", "ueditor");

        State storageState;
        if (result.get("status").equals("success")) {
            storageState = new BaseState(true);
        } else {
            storageState = new BaseState(false);
        }

        if (storageState.isSuccess()) {
            storageState.putInfo("url", result.get("data"));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", originFileName + suffix);
        }

        return storageState;
    }


    public static final State save(Map<String, Object> conf, MultipartFile upfile) {

        try {
            InputStream is = upfile.getInputStream();

            String savePath = (String) conf.get("savePath");
            String originFileName = upfile.getOriginalFilename();//fileStream.getName();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0,
                    originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = (Long) conf.get("maxSize");

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            //String physicalPath = conf.get("rootPath") + savePath;
            String physicalPath = conf.get("uploadRootPath") + savePath;

            State storageState = StorageManager.saveFileByInputStream(is,
                    physicalPath, maxSize);
            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;
        } catch (IOException e) {
            return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
        }
    }


    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
