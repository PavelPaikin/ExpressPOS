package com.dev.lakik.expresspos.Helpers;

import android.util.Log;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class myCrypt {
    private String iv = "rhfyuejdkirjtufj";//Dummy iv
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    
    private String SecretKey = "65hy46uj8dye6m2k";//Dummy secretKey
    
    public myCrypt()
    {
            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
            
            try {
                    cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
    }

    public String encrypt(String text){
        String encryptedText = "";
        try{
            encryptedText = myCrypt.bytesToHex(encryptData(text));
        }catch (Exception ex){
            Log.d("sdad", "Error: " + ex.getMessage());
        }

        return encryptedText;
    }
    
    private byte[] encryptData(String text) throws Exception
    {
            if(text == null || text.length() == 0)
                    throw new Exception("Empty string");
            
            byte[] encrypted = null;

            try {
                    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                    encrypted = cipher.doFinal(padString(text).getBytes());
            } catch (Exception e)
            {                       
                    throw new Exception("[encrypt] " + e.getMessage());
            }
            
            return encrypted;
    }

    public String decrypt(String text){
        String decryptedText = "";
        try{
            decryptedText = myCrypt.bytesToHex(decryptData(text));
        }catch (Exception ex){
            Log.d("sdad", "Error: " + ex.getMessage());
        }

        return decryptedText;
    }
    
    private byte[] decryptData(String code) throws Exception
    {
            if(code == null || code.length() == 0)
                    throw new Exception("Empty string");
            
            byte[] decrypted = null;

            try {
                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                    
                    decrypted = cipher.doFinal(hexToBytes(code));
            } catch (Exception e)
            {
                    throw new Exception("[decrypt] " + e.getMessage());
            }
            return decrypted;
    }
    

    
    public static String bytesToHex(byte[] data)
    {
            if (data==null)
            {
                    return null;
            }
            
            int len = data.length;
            String str = "";
            for (int i=0; i<len; i++) {
                    if ((data[i]&0xFF)<16)
                            str = str + "0" + Integer.toHexString(data[i]&0xFF);
                    else
                            str = str + Integer.toHexString(data[i]&0xFF);
            }
            return str;
    }
    
            
    public static byte[] hexToBytes(String str) {
            if (str==null) {
                    return null;
            } else if (str.length() < 2) {
                    return null;
            } else {
                    int len = str.length() / 2;
                    byte[] buffer = new byte[len];
                    for (int i=0; i<len; i++) {
                            buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                    }
                    return buffer;
            }
    }
    
    

    private static String padString(String source)
    {
      char paddingChar = ' ';
      int size = 16;
      int x = source.length() % size;
      int padLength = size - x;

      for (int i = 0; i < padLength; i++)
      {
              source += paddingChar;
      }

      return source;
    }
}
