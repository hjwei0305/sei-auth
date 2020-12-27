package com.changhong.sei.auth.certification.sso.weaver;

import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-12-27 21:29
 */
public class DesUtil {

    private final static String DES = "DES";
    // des 向量
    private static final byte[] byteIV = {0x12, 0x34, 0x56, 0x78, (byte) 0x90,
            (byte) 0xab, (byte) 0xcd, (byte) 0xef};


    public static void main(String[] args) {
        try {
            System.out.println(encrypt("Admin", "_myhome_"));
            System.out.println(decrypt("XL0E2nT6DS8=", "_myhome_"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Description DES加密
     *
     * @param data
     * @param key  加密键,必须8位以及以上
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {

        if (StringUtils.isEmpty(data)) {
            throw new Exception("加密字符串为空");
        }

        if (StringUtils.isEmpty(key)) {
            throw new Exception("密钥为空");
        }

        if (key.length() != 8) {
            throw new Exception("密钥长度必须为8位");
        }

        byte[] bt = encrypt(data.getBytes("utf-8"), key.getBytes("utf-8"));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * Description DES解密
     *
     * @param data
     * @param key  加密键,必须8位以及以上
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {

        if (StringUtils.isEmpty(data)) {
            throw new Exception("解密字符串为空");
        }

        if (StringUtils.isEmpty(key)) {
            throw new Exception("密钥为空");
        }

        if (key.length() != 8) {
            throw new Exception("密钥长度必须为8位");
        }

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt, "utf-8");
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 加密byte数组
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        IvParameterSpec iv = new IvParameterSpec(byteIV);
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 解密byte数组
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        IvParameterSpec iv = new IvParameterSpec(byteIV);
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
        return cipher.doFinal(data);
    }
}
