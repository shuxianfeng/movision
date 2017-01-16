package com.movision.utils;


import com.oreilly.servlet.multipart.FileRenamePolicy;
import com.movision.utils.file.FileUtil;

import java.io.File;

/**
 * 自定义的命名策略文件
 */
public class RandomFileNamePolicy implements FileRenamePolicy {
    public File rename(File file) {

        return FileUtil.renameFile(file);
    }
}