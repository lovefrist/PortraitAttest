package com.example.portraitattest.utils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtile {
    /**
     * AES加密字符串
     *
     * @param content 需要被加密的字符串
     * @return 密文
     */
    public static String encrypt(String content, String keyStr) {
        try {
            SecretKeySpec key = new SecretKeySpec(Base64.getMimeDecoder().decode(keyStr), "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[16]));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.getMimeEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content AES加密过过的内容
     * @return 明文
     */
    public static String decrypt(String content, String keyStr) {
        try {
            SecretKeySpec key = new SecretKeySpec(Base64.getMimeDecoder().decode(keyStr), "AES");// 转换为AES专用密钥
            System.out.println("解密密钥" + keyStr);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[16]));// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(Base64.getMimeDecoder().decode(content));
            return new String(result, StandardCharsets.UTF_8); // 明文
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAesKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            kgen.init(128, new SecureRandom());
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            return Base64.getMimeEncoder().encodeToString(enCodeFormat);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "数据错误";
    }
}
