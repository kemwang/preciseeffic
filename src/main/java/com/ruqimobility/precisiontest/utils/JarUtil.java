package com.ruqimobility.precisiontest.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarUtil.class);

    /**
     * 解压jar包
     * @param jarFilePath
     */
    public static void unzipJar(String jarFilePath){
        String destDir = jarFilePath.replace(".jar","");
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }
        else{
            return;
        }
        try {
            ZipFile unzipJar = new ZipFile(jarFilePath);
            unzipJar.extractAll(destDir);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractFile(JarInputStream jarIn, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = jarIn.read(bytesIn)) != -1) {
                fos.write(bytesIn, 0, read);
            }
        }
    }

    public static void compressDirectoryToJar(String sourceDir, String jarFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(jarFileName);
        JarOutputStream jos = new JarOutputStream(fos);
        compressDirectoryToJar(new File(sourceDir), "", jos);
        jos.close();
        fos.close();
    }

    private static void compressDirectoryToJar(File file, String parentPath, JarOutputStream jos) throws IOException {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                compressDirectoryToJar(child, parentPath + file.getName() + "/", jos);
            }
        } else {
            jos.putNextEntry(new JarEntry(parentPath + file.getName()));
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                jos.write(buffer, 0, bytesRead);
            }
            fis.close();
            jos.closeEntry();
        }
    }
}
