package com.seleniumframework.utilities;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptData {

	// private static final byte[] SECRET_KEY = "YourSecureKey123".getBytes();
	private static final byte[] SECRET_KEY = "SeleniumJava2026".getBytes();
	private static final String ALGORITHM = "AES";
	
	// Method to encrypt data
	public static String encryptData(String data) throws Exception {
		SecretKeySpec key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);

	}

	// Method to decrypt data
	public static String decryptData(String data) throws Exception {
		SecretKeySpec key = new SecretKeySpec(SECRET_KEY, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
		return new String(decryptedBytes);

	}

}
