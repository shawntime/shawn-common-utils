package com.shawntime.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * DES 加解密工具类
 * <p/>
 * Created by chengwenjun on 2016/3/11.
 */
public final class CryptoUtils {


    private CryptoUtils() {
    }

    /**
     * 用户.net平台的解密
     *
     * @param message
     * @param desPrivateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String desPrivateKey) throws Exception {
        byte[] bytesrc = toHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(desPrivateKey.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(desPrivateKey.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return URLDecoder.decode(new String(retByte), "UTF-8");
    }

    public static byte[] toHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }


    private static byte[] iv1 = {(byte) 0x12, (byte) 0x34, (byte) 0x56,
            (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};


    /**
     * 解密
     *
     * @param decryptString
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey)
            throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        return new String(cipher.doFinal(Base64.decode(decryptString)));
    }

    /**
     * 加密
     *
     * @param encryptString
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String encryptDES(String encryptString, String encryptKey)
            throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        DESKeySpec dks = new DESKeySpec(encryptKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        return Base64.encode(cipher.doFinal(encryptString.getBytes()));
    }

}
