//classe que facilita a criptografia de descriptografia

package com.api.boleto.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.api.boleto.model.exceptions.ResourceInternalServerError;

public class CryptAesUtil {
	
	private static final String ALGORITHM = "AES";
	
	public static SecretKeySpec getKey() throws Exception{
		String secretKey = "$aes.chave";
		if(secretKey == null||secretKey.isEmpty()) {
			throw new ResourceInternalServerError("Chave secreta não foi definida.");
		}
		byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		key = sha.digest(key);
		return new SecretKeySpec(key, ALGORITHM);
	}
	
	public static String encrypt(String data) throws Exception{
		SecretKeySpec keySpec = getKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encryptData = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptData);
	}

	public static String decrypt(String value) throws Exception{
		SecretKeySpec keySpec = getKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decryptData = cipher.doFinal(Base64.getDecoder().decode(value));
		return new String(decryptData);
	}
}
