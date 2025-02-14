package com.ruqimobility.precisiontest.utils;

import com.ruqimobility.precisiontest.constants.enums.StatusCode;
import com.ruqimobility.precisiontest.exception.PrecisitionTestException;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.ruqimobility.precisiontest.constants.GitlabConstant.*;

/**
 * @author lvzhiqi
 * @date 2023/5/11
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建文件夹
     */
    public static void createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists() && !folder.isDirectory()) {
            LOGGER.info("创建文件夹：" + folderName);
            String[] cmd = {"mkdir", "-p", folderName};
            List<String> result = Arrays.asList(cmd);
            try {
                Path path = Paths.get(folderName);
                Files.createDirectory(path);
            } catch (IOException e) {
                throw PrecisitionTestException.build(e.getMessage() + "\n" + "创建文件夹异常", StatusCode.CREATE_FOLDER_ERROR);
            }
            LOGGER.info(String.valueOf(result));
        }
        else {
            LOGGER.info("文件夹已存在");
        }
    }

    /**
     * 删除文件 或者 清除文件夹下的所有文件
     * @param filePath
     */
    public static void deleteFile(String filePath)
    {
        File file = new File(filePath);
        //判断是否为文件，是，则删除
        if (file.isFile())
        {
            file.delete();
        }
        else //不为文件，则为文件夹
        {
            //获取文件夹下所有文件相对路径
            String[] childFilePath = file.list();
            if (childFilePath != null) {
                for (String path:childFilePath)
                {
                    //递归，对每个都进行判断
                    deleteFile(file.getAbsoluteFile()+"/"+path);
                }
            }
            file.delete();
        }
    }

    /**
     * 解析pom.xml中的打包包名
     * @param path
     * @return
     */
    public static String getPackageStrFromPom(String path) {
        //创建解析器
        SAXReader saxReader = new SAXReader();
        Document pomDoc = null;
        //解析指定的XML文件
        try {
            pomDoc = saxReader.read(new File(path + POM_FILE));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootEle = pomDoc.getRootElement();
        String nsUri = rootEle.getNamespaceURI();
        Map nsMap = new HashMap();
        nsMap.put("rootNs", nsUri);
        XPath mesXpath = pomDoc.createXPath("//rootNs:project/rootNs:build/rootNs:finalName");
        mesXpath.setNamespaceURIs(nsMap);
        Node finalNode = mesXpath.selectSingleNode(pomDoc);
        if(Objects.isNull(finalNode)){
            mesXpath = pomDoc.createXPath("//rootNs:project/rootNs:artifactId");
            mesXpath.setNamespaceURIs(nsMap);
            Node pNode = mesXpath.selectSingleNode(pomDoc);
            return pNode.getText();
        }
        else {
            String finalName = finalNode.getText();
            if(finalName.contains(FINAL_NAME_VAR)) {
                mesXpath = pomDoc.createXPath("//rootNs:project/rootNs:artifactId");
                mesXpath.setNamespaceURIs(nsMap);
                Node pNode = mesXpath.selectSingleNode(pomDoc);
                return pNode.getText();
            }
            return finalName;
        }
    }

    /**
     * 按关键字查找文件
     * @param folder
     * @param keyword
     * @return
     */
    public static List<File> searchFiles(File folder,  String keyword) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile()) {
            result.add(folder);
        }

        File[] subFolders = folder.listFiles(file -> {
            if (file.isDirectory()) {
                return true;
            }
            return file.getName().toLowerCase().contains(keyword);
        });
        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file, keyword));
                }
            }
        }
        return result;
    }

    public static List<String> searchFiles(String directoryPath, String keyword) {
        List<String> foundFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().contains(keyword)) {
                        foundFiles.add(directoryPath + "/" +file.getName());
                    }
                }
            }
        }

        return foundFiles;
    }

    /**
     * 拷贝目录及文件
     * @param srcFile
     * @param destFile
     */
    public static void copyFiles(File srcFile,File destFile) {

        if (srcFile.isFile()) {
            //是文件的话，就要开始拷贝
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(srcFile);

                String path = (destFile.getAbsolutePath().endsWith("\\") ? destFile.getAbsolutePath() : destFile.getAbsolutePath() + "\\") + srcFile.getAbsolutePath().substring(3);
                out = new FileOutputStream(path);

                byte[] bytes = new byte[1024 * 1024];
                int readCount = 0;
                while ((readCount = in.read(bytes)) != -1) {
                    out.write(bytes, 0, readCount);
                }
                //刷新
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return;
        }

        File[] files = srcFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //新建对应目录
                //新建目录要获取目录的绝对路径，再创建
                String srcDir = file.getAbsolutePath();
                String destDir = (destFile.getAbsolutePath().endsWith("\\") ? destFile.getAbsolutePath() : destFile.getAbsolutePath() + "\\") + srcDir.substring(3);
                File newFile = new File(destDir);
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }
            }
            copyFiles(file, destFile);
            //此目录下可能还有字目录，往里面深挖
        }
    }

    /**
     * 移动一个文件夹内的所有内容到另一个文件夹
     * @param sourceFolder
     * @param targetFolder
     */
    public static void moveFolderContents(String sourceFolder, String targetFolder) {
        try {
            Files.walk(Paths.get(sourceFolder))
                    .forEach(source -> {
                        try {
                            Path target = Paths.get(targetFolder, source.toString().substring(sourceFolder.length()));
                            if (Files.isDirectory(source)) {
                                Files.createDirectories(target);
                            } else {
                                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int dirCount(String dirPath){
        // 获取当前目录
        File currentDirectory = new File(dirPath);
        // 获取当前目录下的所有文件和文件夹
        File[] files = currentDirectory.listFiles();
        int folderCount = 0;

        // 遍历文件数组，统计文件夹个数
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    folderCount++;
                }
            }
        }
        return folderCount;
    }

}
