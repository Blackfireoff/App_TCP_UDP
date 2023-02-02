package com.example.sae302_heron;

import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class EncryptionAES {
    private String message;
    private Key key;
    private byte[] Message;
    EncryptionAES(String message, Key key) {

    this.message =message;
    this.key =key;
        Message =new byte[0];

    encryptMessage();

}


    protected void encryptMessage() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            Message = cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getEncryptedMessage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(Message);
        }
        return null;
    }



    protected void decryptMessage() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Message = cipher.doFinal(Base64.getDecoder().decode(Message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getDecryptedMessage() {
            return new String(Message);
        }

}
