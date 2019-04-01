package com.zxhd.proerp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjing on 2019-04-01
 * describe:截取一个字符串中所需要的值
 */

public class WordHandle {

    public static String regEx_chinese = "[\\u4e00-\\u9fa5]";//提取中文
    public static String regEx_num = "[0-9]";//提取数字
    public static String regEx_english = "[a-zA-Z0-9]";//提取英文
    public static String regEx_english_num = "[a-zA-Z0-9]";//提取英文和数字

    public static String getWord(String str,String regEx) {
//        String str = "……^1dsf  の  adS   DFASFSADF阿德斯防守对方asdfsadf37《？：？@%#￥%#￥%@#$%#@$%^><?1234";
//        String regEx = "[a-zA-Z0-9\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(m.group());
        }
        return sb.toString();
    }
}
