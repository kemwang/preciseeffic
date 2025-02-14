package com.ruqimobility.precisiontest.utils;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lvzhiqi
 * @date 2023/5/10
 */
public class HandleJarUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleJarUtil.class);

    /**
     * 解压JAR包
     * @param dir
     * @param jarName
     * @return
     */
    public static String unzipJar(String dir, String jarName) {
        String unzipDir = dir + "/" + jarName.replace(".jar", "");
        try {
            ZipFile unzipJar = new ZipFile(dir + "/" + jarName);
            unzipJar.extractAll(unzipDir);
        } catch (Exception e) {
            LOGGER.error("dir:{}, jarName:{},unzipJar ERROR：{}", dir,jarName, e.getMessage());
            throw new RuntimeException(e);
        }
        return unzipDir;
    }

    /**
     * 拷贝jar包
     * @param sourcePath
     * @param jarName
     * @param targetPath
     * @throws IOException
     */
    public static void copyJar(String sourcePath, String jarName, String targetPath) throws IOException {
        String sourceJarPath = sourcePath + "/" + jarName;
        String targetJarPath = targetPath + "/" + jarName;
        FileUtils.copyFile(new File(sourceJarPath), new File(targetJarPath));
        unzipJar(targetPath, jarName);
    }


}
