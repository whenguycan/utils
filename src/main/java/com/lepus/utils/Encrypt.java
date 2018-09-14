package com.lepus.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author wangchenyu
 * 2016-11-16
 */
public class Encrypt {
	
	private static final String ENCRYPT_ALGORITHM = "AES";
	private static final int ENCRYPT_KEY_LEN = 128;
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * byte数组转16进制字符串
	 * @param bytes
	 * @return
	 */
	private static String bytes2Hex(byte[] bytes){
		StringBuilder hex = new StringBuilder();
		for(byte b : bytes){
			String h = Integer.toHexString(b);
			if(b > 0 && b < 16){
				hex.append("0");
			}
			hex.append(h.length()==8?h.substring(6):h);
		}
		return hex.toString();
	}
	
	/**
	 * 16进制字符串转byte数组
	 * @param hex
	 * @return
	 */
	private static byte[] hex2Bytes(String hex){
		byte[] bytes = new byte[hex.length()/2];
		for(int i=0, len=bytes.length; i<len; i++){
			bytes[i] = (byte)Integer.parseInt(hex.substring(i*2, i*2+2), 16);
		}
		return bytes;
	}
	
	/**
	 * 随机生成密钥
	 * @return
	 */
	private static byte[] generateSecretKey(String encrypt_algorithm, int encrypt_key_len){
		try {
			KeyGenerator kgen = KeyGenerator.getInstance(encrypt_algorithm);
			kgen.init(encrypt_key_len);
			return kgen.generateKey().getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密
	 * @param password
	 * @param secretKey
	 * @return
	 */
	private static String encrypt(String password, String secretKey){
		byte[] passwordBytes = password.getBytes();
		byte[] secretKeyBytes = hex2Bytes(secretKey);
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKeyBytes, ENCRYPT_ALGORITHM));
			byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
			return bytes2Hex(encryptedPasswordBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param encryptedPassword
	 * @param secretKey
	 * @return
	 */
	private static String decrypt(String encryptedPassword, String secretKey){
		byte[] encryptedPasswordBytes = hex2Bytes(encryptedPassword);
		byte[] secretKeyBytes = hex2Bytes(secretKey);
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKeyBytes, ENCRYPT_ALGORITHM));
			byte[] passwordBytes = cipher.doFinal(encryptedPasswordBytes);
			return new String(passwordBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密
	 * @param password
	 * @return
	 */
	public static String encrypt(String password){
		byte[] secretKeyBytes = generateSecretKey(ENCRYPT_ALGORITHM, ENCRYPT_KEY_LEN);
		String secretKey = bytes2Hex(secretKeyBytes);
		String encryptedPassword = encrypt(password, secretKey);
		//加密字符串拼接，注意，解密时获取密钥要参照这里
		return secretKey + encryptedPassword;
	}
	
	/**
	 * 解密
	 * @param encryptedPassword
	 * @return
	 */
	public static String decrypt(String encryptedPassword){
		int secretKeySize = ENCRYPT_KEY_LEN / 4;
		String secretKey = encryptedPassword.substring(0, secretKeySize);
		String encryptedPassword_ = encryptedPassword.substring(secretKeySize);
		return decrypt(encryptedPassword_, secretKey);
	}
	
	/**
	 * md5
	 * @param text
	 * @return
	 */
	public static String md5(String text){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] bytes = md.digest();
			return bytes2Hex(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param file
	 * @param ignore
	 * @return
	 */
	public static String md5(File file, String... ignore){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = reader.readLine()) != null){
				for(String regex : ignore){
					line = line.replaceAll(regex, "");
				}
				md.update(line.getBytes());
			}
			reader.close();
			return bytes2Hex(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * md5
	 * @param file
	 * @return
	 */
	public static String md5(File file){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[256];
			int len = -1;
			while((len = is.read(buffer)) != -1){
				md.update(buffer, 0, len);
			}
			is.close();
			return bytes2Hex(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 密码验证
	 * @param password
	 * @param passwordStored
	 * @return
	 */
	public static boolean checkPassword(String password, String passwordStored){
		String salt = getSaltFromStoredPassword(passwordStored);
		return passwordStored.equals(makePassword(salt, password));
	}
	
	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	public static String encodePassword(String password){
		String salt = generateSalt();
		return makePassword(salt, password);
	}
	
	private static String generateSalt(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 7);
	}
	private static String getSaltFromStoredPassword(String passwordStored){
		String p = passwordStored.substring(0, 2);
		String s = passwordStored.substring(passwordStored.length() - 5);
		return p + s;
	}
	private static String makePassword(String salt, String password){
		String p = salt.substring(0, 2);
		String s = salt.substring(salt.length() - 5);
		return p + md5(s + password + p) + s;
	}
	
	public static void main(String[] args){
		String x = "aaabbb";
		String y = "aaabbb\r\n";
		System.out.println(encrypt(x));
		System.out.println(encrypt(y));
	}
}
