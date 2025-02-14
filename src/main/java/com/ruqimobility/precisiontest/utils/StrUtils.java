package com.ruqimobility.precisiontest.utils;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ruqimobility.precisiontest.constants.GitlabConstant.COLON;

public class StrUtils {
    //判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0)+"");
        return isNum.matches();
    }

    /**
     * 正则匹配验证字符串是否是版本号
     * @param str
     * @return
     */
    public static boolean isPackageVersionStr(String str) {
        String pattern = "^([0-9]d*.?d*)+";
        Pattern r = Pattern.compile(pattern);
        Matcher isVersion = r.matcher(str);
        return isVersion.matches();
    }

    public static boolean isChinese( char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true ;
        }
        return false ;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile( "\\s*|\t*|\r*|\n*" );
        Matcher m = p.matcher(strName);
        String after = m.replaceAll( "" );
        String temp = after.replaceAll( "\\p{P}" , "" );
        char [] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0 ;
        for ( int i = 0 ; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1 ;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4 ) {
            return true ;
        } else {
            return false ;
        }

    }

    /**
     * 使用正则表达式提取中括号中的内容
     * @param content
     * @return
     */
    public static String[] getBracketContent(String content){
        String [] arr = new String[0];
        Pattern p = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher m = p.matcher(content);
        while(m.find()){
            arr = Arrays.copyOf(arr,arr.length+1);
            arr[arr.length-1]=m.group();
        }
        return arr;
    }

    //去除所有空格
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            /*\n 回车(\u000a)
            \t 水平制表符(\u0009)
            \s 空格(\u0008)
            \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }

    public static String removeAnnotation(String str) {
        if(!str.contains("@")){
            return str;
        }
        int index = str.indexOf("@");
        return str.substring(0,index);
    }


    /**
     * 去除多余的括号以及括号内内容
     * @param str
     * @return
     */
    public static String removeUnnecessaryStr(String str) {
        str = replaceAllBlank(str);
        if(str.contains("@")){
            return str;
        }
        char startch = '(';
        char endch = ')';
        List<String> indexList = new ArrayList<>();
        int startIndex = str.indexOf(startch);
        int endIndex = str.indexOf(endch);
        while (startIndex != -1 && endIndex != -1) {
            indexList.add(startIndex + "," + endIndex);
            startIndex = str.indexOf(startch, startIndex + 1);
            endIndex = str.indexOf(endch, endIndex + 1);

        }
        if (indexList.size()>1) {
            for(int i = 1; i< indexList.size(); i++){
                String[] indexArray = indexList.get(i).split(",");
                int start = Integer.parseInt(indexArray[0]);
                int end = Integer.parseInt(indexArray[1]);
                try {
                    String removeStr = str.substring(start, end+1);
                    str = str.replace(removeStr, "");
                }
                catch (Exception e){
                    System.err.println(str);
                }
            }
            return str;
        }
        else {
            return str;
        }
    }

    //
    public static int findFirstIndex(String str, String f) {
        Matcher matcher=Pattern.compile(f).matcher(str);
        if(matcher.find()){
            return matcher.start();
        }else{
            return 0;
        }
    }

    public static String getBracketsContent(String str, String startChar, String endChar) {
        int start = 0;
        int end = 0;
        if(str.contains(startChar) && str.contains(endChar)){
            start = str.indexOf(startChar);
            end = str.indexOf(endChar);
            return str.substring(start+1, end);
        }
        return str;
    }

    public static String removeBracketsAndContent(String str, String startChar, String endChar) {
        int start = 0;
        int end = 0;
        while(str.contains(startChar) && str.contains(endChar)){
            start = str.indexOf(startChar);
            end = str.indexOf(endChar);
            while(str.length() > (end+1) && endChar.equals(Character.toString(str.charAt(end + 1)))){
                end = end + 1;
            }
            String subStr = str.substring(start,end+1);
            str =  str.replace(subStr, "");
        }
        return str;
    }

    public static String removeAllBracketsAndContent(String str, String startChar, String endChar) {
        if (str.contains(startChar)) {
            int cnt = str.length()-str.replaceAll("\\" + startChar,"").length();
            for (int i = 0; i < cnt; i++) {
                str =  removeBracketsAndContent(str,startChar,endChar);
            }
            return str;
        }
        else {
            return str;
        }

    }

    public static String getFullClassName(String str) {
        if(Objects.isNull(str)){
            return null;
        }
        String newStr = str.substring(str.indexOf("java/")+5);
        return newStr.replaceAll("/", ".").replace(".java","");
    }


    public static String parseClassName(String fullName, String delimiter){
        String[] temp = fullName.split(delimiter);
        temp = Arrays.copyOf(temp,temp.length-1);
        return String.join(delimiter, temp);
    }


    public static String getMethodName(String fullMethod){
        if(fullMethod==null || "".equals(fullMethod)){
            return "";
        }
        String[] temp = fullMethod.split(":");
        return temp[temp.length - 1];
    }

    public static String getClassName(String fullName, String delimiter){
        String[] temp = fullName.split(delimiter);
        temp = Arrays.copyOf(temp,temp.length-1);
        String newString = String.join(delimiter, temp);
        String[] tempNewString =  newString.split("\\.");
        return tempNewString[tempNewString.length-1];
    }

    public static String newParamMethodString(String line) {
        String prefix = "";
        if(line.contains(COLON)){
            prefix = line.split(COLON)[0] + COLON;
            line = line.split(COLON)[1];
        }
        StringBuilder sb = new StringBuilder();
        String[] paramStrArray = StrUtils.getBracketContent(line);
        if (paramStrArray.length < 1) {
            sb.append(line);
        } else {
            String paramStr = paramStrArray[0];
            String tempLine = line;
            if (!paramStr.contains("/")) {
                String[] params = paramStr.split(",");
                for (String param : params) {
                    String[] array = param.split("\\.");
                    String newParam = array[array.length - 1];
                    tempLine = tempLine.replace(param, newParam);
                }
                sb.append(tempLine);
            }
            else {
                sb.append(line);
            }
        }
        return prefix + sb.toString();
    }

    //opt = 0/1/2 分别对应新增、删除、rename
    public static String formatDiffs(String diffs, String oldFile, String newFile, Integer opt){
        if(diffs==null){
            return null;
        }
        String delOps = "+++ /dev/null";
        String newOps = "--- /dev/null";

        String result = "";
        switch (opt){
            case 0:
                result =  String.format("%s"+System.lineSeparator()+"+++ b/%s"+System.lineSeparator()+"%s",newOps,newFile,diffs);
                break;
            case 1:
                result =  String.format("--- a/%s"+System.lineSeparator()+"%s"+System.lineSeparator()+"%s",oldFile,delOps,diffs);
                break;
            case 2:
                result =  String.format("--- a/%s"+System.lineSeparator()+"+++ b/%s"+System.lineSeparator()+"%s",oldFile,newFile,diffs);
                break;
        }
        return result;

    }

    public static String diffGitFiles(List<String> original, List<String> revised, String oldFilePath, String newFilePath){
        if(!"/dev/null".equals(oldFilePath)){
            oldFilePath = "a/" + oldFilePath;
        }
        if(!"/dev/null".equals(newFilePath)){
            newFilePath = "b/" + newFilePath;
        }
        Patch<String> patch = DiffUtils.diff(original,revised);
        List<String> unifiedList = UnifiedDiffUtils.generateUnifiedDiff(oldFilePath, newFilePath, original, patch, 0);
        return String.join("\n", unifiedList);
    }
}
