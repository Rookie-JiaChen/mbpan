package com.mbrickspan.mbpan.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final char[] hexDigest = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    /**
     * MD5加密算法
     * @param content
     * @return
     */
    public static String md5(String content)
    {
        try {
            //1. 返回实现指定摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            //2.获取待加密字符串的byte数组
            byte[] bytes = content.getBytes();

            //3.使用指定的 byte 数组更新摘要
            md.update(bytes);

            //4.加密： 通过执行诸如填充之类的最终操作完成哈希计算
            byte[] newBytes = md.digest();    //得到长度为16的字节数组

            //5.将长度为16的byte数组表示为32位十六进制数字
            /*byte a = -81;
            System.out.println(Integer.toBinaryString(a));      //111111111111111111111111  1010 1111  字节有效位8位
            System.out.println(Integer.toBinaryString(a >>> 4 & 0xf));//111111111111111111111111  1010  无符号右移四位，与0xf做与运算，去24个1
            System.out.println(Integer.toBinaryString(a & 0xf));//1111
*/
            //System.out.println(Integer.toHexString(a >>> 4 & 0xf));
            //System.out.println(Integer.toHexString(a & 0xf));

            char[] result = new char[newBytes.length * 2];//定义存储十六进制数结果的字符数组
            int k = 0;
            for (int i = 0; i < newBytes.length; i++) {
                byte c = newBytes[i];
                result[k++] = hexDigest[c >>> 4 & 0xf];//高四位
                result[k++] = hexDigest[c & 0xf]; //低四位
            }

            return new String(result);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}