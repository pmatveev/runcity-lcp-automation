package org.runcity.util;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.Key;
import java.security.InvalidKeyException;

public class PasswordCipher {
	private static final String algorithm = "DES";
	private static Key key = null;
	private static Cipher cipher = null;

	public PasswordCipher(String phrase) throws Exception {
		key = new SecretKeySpec(phrase.getBytes(), algorithm);
		cipher = Cipher.getInstance(algorithm);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("USAGE: java PasswordCipher [key] [string]");
			System.exit(1);
		}

		if (args[0].length() < 8) {
			System.out.println("Invalid key: " + args[0]);
			System.exit(1);
		}
		
		PasswordCipher c = new PasswordCipher(args[0].substring(0, 8));
		System.out.println("Encoded value: " + Hex.encodeHexString(c.encrypt(args[1])));
	}

	private byte[] encrypt(String input)
			throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input.getBytes());
	}

	public String decrypt(String input)
			throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DecoderException {
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Hex.decodeHex(input)));
	}
}