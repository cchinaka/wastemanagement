package com.milcomsolutions.commons.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;


public class EncryptionHelper {

    private static final String DES_CIPHER = "DES";

    private static final String UTF8_KEY = "UTF8";

    private static final String PASS_PHRASE = "attahemmanaud";


    public static String encrypt(String msg) {
        try {
            KeySpec keySpec = new DESKeySpec(EncryptionHelper.PASS_PHRASE.getBytes());
            SecretKey key = SecretKeyFactory.getInstance(EncryptionHelper.DES_CIPHER).generateSecret(keySpec);
            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] utf8 = msg.getBytes(EncryptionHelper.UTF8_KEY);
            byte[] enc = ecipher.doFinal(utf8);
            return new String(Base64.encodeBase64(enc));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void encryptOrDecryptUNREADY(int mode, InputStream is, OutputStream os) throws Throwable {
        KeySpec keySpec = new DESKeySpec(EncryptionHelper.PASS_PHRASE.getBytes());
        SecretKey desKey = SecretKeyFactory.getInstance(EncryptionHelper.DES_CIPHER).generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance(desKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, desKey);
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            EncryptionHelper.doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            EncryptionHelper.doCopy(is, cos);
        }
    }


    public static void main(String[] args) {
        String encrypted = EncryptionHelper.encrypt("password");
        System.out.println(encrypted);
        System.out.println(EncryptionHelper.decrypt(encrypted));
    }


    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }


    public static String decrypt(String msg) {
        try {
            KeySpec keySpec = new DESKeySpec(EncryptionHelper.PASS_PHRASE.getBytes());
            SecretKey key = SecretKeyFactory.getInstance(EncryptionHelper.DES_CIPHER).generateSecret(keySpec);
            Cipher decipher = Cipher.getInstance(key.getAlgorithm());
            decipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = Base64.decodeBase64(msg.getBytes());// new sun.misc.BASE64Decoder().decodeBuffer(msg);
            byte[] utf8 = decipher.doFinal(dec);
            return new String(utf8, EncryptionHelper.UTF8_KEY);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encryptURL(String menuUrl) {
        String retVal = menuUrl;
        int paracnt = menuUrl.trim().indexOf("?");
        if (paracnt > 0) {
            String plainStringmenu = menuUrl.substring(0, paracnt);
            String plainString = (menuUrl.substring(paracnt + 1, menuUrl.trim().length()));
            String encryptedS = menuUrl.substring(paracnt + 1, menuUrl.trim().length());
            try {
                encryptedS = "_asha=" + EncryptionHelper.encodeURL(EncryptionHelper.encrypt(plainString));
            } catch (Exception fe) {
                System.out.println("MenuFile Problem: " + fe.getMessage());
            }
            menuUrl = plainStringmenu + "?" + encryptedS;
            retVal = menuUrl;
        }
        return retVal;
    }
}
