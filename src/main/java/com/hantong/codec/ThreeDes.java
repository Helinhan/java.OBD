package com.hantong.codec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ThreeDes {
    private static final String Algorithm = "DESede";
    private static final byte[] keyBytes={
            (byte) 0x8C,(byte) 0xEB,(byte) 0xDC,(byte) 0xB2,
            (byte) 0xC4,(byte) 0x2A,(byte) 0x3B,(byte) 0xA2,
            (byte) 0x6F,(byte) 0xE8,(byte) 0x79,(byte) 0xCF,
            (byte) 0xFE,(byte) 0x7C,(byte) 0x75,(byte) 0x6B,
            (byte) 0x8F,(byte) 0x19,(byte) 0x43,(byte) 0xEF,
            (byte) 0x86,(byte) 0x69,(byte) 0x4A,(byte) 0x81,
    };


    public static byte[] encryptMode(byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm+"/ECB/NoPadding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;

    }


    public static byte[] decryptMode( byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm+"/ECB/NoPadding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (javax.crypto.IllegalBlockSizeException e2) {
            e2.printStackTrace();
        }catch (Exception e3) {

            e3.printStackTrace();
            //System.out.println(src.toString());
        }
        return null;
    }





    // DES,DESede,Blowfish
    // keybyte为加密密钥，长度为24字节
    // src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm+"/ECB/NoPadding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;

    }
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm+"/ECB/NoPadding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (javax.crypto.IllegalBlockSizeException e2) {
            e2.printStackTrace();
        }catch (Exception e3) {

            e3.printStackTrace();
            //System.out.println(src.toString());
        }
        return null;
    }

}
