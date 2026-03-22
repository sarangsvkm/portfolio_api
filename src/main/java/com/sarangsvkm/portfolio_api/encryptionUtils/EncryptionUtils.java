package com.sarangsvkm.portfolio_api.encryptionUtils;

import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
@Component
public class EncryptionUtils {
	
  private static final String ALGORITHM = "AES";
  // AES requires a 16, 24, or 32 byte key. (16 characters = 16 bytes for a 128-bit key)
  private static final String KEY ="ThisIsSpartaThis"; 

  public String encrypt(String data) throws Exception 
  {
      if (data == null) return null;
      SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  public String decrypt(String encryptedData) throws Exception {
      if (encryptedData == null) return null;
      SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
      return new String(decryptedBytes, StandardCharsets.UTF_8);
  }
  


}
