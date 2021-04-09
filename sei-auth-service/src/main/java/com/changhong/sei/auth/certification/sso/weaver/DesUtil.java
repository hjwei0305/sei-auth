package com.changhong.sei.auth.certification.sso.weaver;

import com.changhong.sei.util.EncodeUtil;
import com.changhong.sei.util.IdGenerator;
import com.changhong.sei.util.RSAUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-12-27 21:29
 */
public class DesUtil {

    private final static String DES = "DES";
    public final static String DEFAULT_KEY = "sei_auth";
    // des 向量
    private static final byte[] BYTEIV = {0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef};

    public static void main(String[] args) {
        try {
            String s = IdGenerator.uuid2();
            System.out.println(s);
            s = encrypt(s, DEFAULT_KEY);
            System.out.println(s);
            System.out.println(URLEncoder.encode(s, "utf-8"));
            System.out.println(decrypt(s, DEFAULT_KEY));

            System.out.println();
            System.out.println("----------------");
            System.out.println();

            String s1 = IdGenerator.uuid2();
            System.out.println(s1);
            s1 = EncodeUtil.aesEncrypt(s1, DEFAULT_KEY);
            System.out.println(s1);
            System.out.println(URLEncoder.encode(s1, "utf-8"));
            s1 = EncodeUtil.aesDecrypt(s1, DEFAULT_KEY);
            System.out.println(s1);

            System.out.println();
            System.out.println("----------------");
            System.out.println();

            Map<String, String> keyMap = RSAUtils.getKeys();
            String message = IdGenerator.uuid2();;
            String result = RSAUtils.encryptByPrivateKey(message, keyMap.get(RSAUtils.PRIVATE_KEY));
            System.out.println(result);
            System.out.println(RSAUtils.decryptByPublicKey(result, keyMap.get(RSAUtils.PUBLIC_KEY)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description DES加密
     *
     * @param key 加密键,必须8位以及以上
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

        byte[] bt = encrypt(data.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
        String strs = Base64Utils.encodeToString(bt);
        return strs;
    }

    /**
     * Description DES解密
     *
     * @param key 加密键,必须8位以及以上
     */
    public static String decrypt(String data, String key) throws IOException, Exception {

        if (StringUtils.isEmpty(data)) {
            throw new Exception("解密字符串为空");
        }

        if (StringUtils.isEmpty(key)) {
            throw new Exception("密钥为空");
        }

        if (key.length() != 8) {
            throw new Exception("密钥长度必须为8位");
        }

        byte[] buf = Base64Utils.decodeFromString(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt, StandardCharsets.UTF_8);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 加密byte数组
     * @param key  加密键byte数组
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        IvParameterSpec iv = new IvParameterSpec(BYTEIV);
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
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        IvParameterSpec iv = new IvParameterSpec(BYTEIV);
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
