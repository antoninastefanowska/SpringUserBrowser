package com.antonina.springapp.implementations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.antonina.springapp.interfaces.IHashService;

@Service
public class HashService implements IHashService {
	private MessageDigest digest;
	
	public HashService() {
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getHash(String s) {
		byte[] encoded = digest.digest(s.getBytes(StandardCharsets.UTF_8));
		StringBuilder builder = new StringBuilder(2 * encoded.length);
		for (int i = 0; i < encoded.length; i++) {
			String hex = Integer.toHexString(0xff & encoded[i]);
			if (hex.length() == 1)
				builder.append('0');
			builder.append(hex);
		}
		return builder.toString();
	}

}
