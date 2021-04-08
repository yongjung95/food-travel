package com.food.foodtravel.config.auth.seed;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Seed {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static String pbszUesrKey;
    private static String pbszIV;

    @Value("${pbszUserKey}")
    public void setpbszUserKey(String pbszUserKey) {
        pbszUesrKey = pbszUserKey;
    }

    @Value("${pbszIV}")
    public void setpbszIV(String IV) {
        pbszIV = IV;
    }


    public static String encrypt(String rawMessage) {

        byte[] pbszUesrKeyBytes = pbszUesrKey.getBytes();
        byte[] pbszIVBytes = pbszIV.getBytes();


        Base64.Encoder encoder = Base64.getEncoder();
        byte[] message = rawMessage.getBytes(UTF_8);
        byte[] encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUesrKeyBytes, pbszIVBytes, message, 0, message.length);

        return new String(encoder.encode(encryptedMessage), UTF_8);
    }

    public static String decrypt(String encryptedMessage) {
        byte[] pbszUesrKeyBytes = pbszUesrKey.getBytes();
        byte[] pbszIVBytes = pbszIV.getBytes();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] message = decoder.decode(encryptedMessage);
        byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUesrKeyBytes, pbszIVBytes, message, 0, message.length);
        return new String(decryptedMessage, UTF_8);
    }

}
