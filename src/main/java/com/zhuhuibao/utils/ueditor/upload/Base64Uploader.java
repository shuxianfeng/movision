package com.zhuhuibao.utils.ueditor.upload;


import java.util.Map;

import com.zhuhuibao.utils.ueditor.PathFormat;
import com.zhuhuibao.utils.ueditor.define.AppInfo;
import com.zhuhuibao.utils.ueditor.define.BaseState;
import com.zhuhuibao.utils.ueditor.define.FileType;
import com.zhuhuibao.utils.ueditor.define.State;
import org.apache.commons.codec.binary.Base64;

public final class Base64Uploader {

    public static State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse((String) conf.get("savePath"),
                (String) conf.get("filename"));

        savePath = savePath + suffix;
//		String physicalPath = conf.get("rootPath") + savePath;
        String physicalPath = conf.get("uploadRootPath") + savePath;


        State storageState = StorageManager.saveBinaryFile(data, physicalPath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url", PathFormat.format(savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}