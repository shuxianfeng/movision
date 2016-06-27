package com.zhuhuibao.utils;

import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.zhuhuibao.utils.file.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * 自定义的命名策略文件
 */
public class RandomFileNamePolicy implements FileRenamePolicy {
    public File rename(File file) {

        return FileUtil.renameFile(file);
    }
}