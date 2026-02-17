package com.geheimnis.metodosCriptografia;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Arrays;

import com.geheimnis.abstracts.Criptografia;

public class AESGCM implements Criptografia {

    private static final int SALT_SIZE = 16;
    private static final int IV_SIZE = 12;
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 600_000;

    @Override
    public byte[] encrypt(byte[] data, String senha) throws Exception {

        
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        SecretKey key = deriveKey(senha, salt);

        
        byte[] iv = new byte[IV_SIZE];
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] cipherText = cipher.doFinal(data);

        byte[] result = new byte[salt.length + iv.length + cipherText.length];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(iv, 0, result, salt.length, iv.length);
        System.arraycopy(cipherText, 0, result, salt.length + iv.length, cipherText.length);

        return result;
    }

    @Override
    public byte[] decript(byte[] data, String senha) throws Exception {

        byte[] salt = Arrays.copyOfRange(data, 0, SALT_SIZE);
        byte[] iv = Arrays.copyOfRange(data, SALT_SIZE, SALT_SIZE + IV_SIZE);
        byte[] cipherText = Arrays.copyOfRange(data, SALT_SIZE + IV_SIZE, data.length);

        SecretKey key = deriveKey(senha, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        return cipher.doFinal(cipherText);
    }

    private SecretKey deriveKey(String senha, byte[] salt) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
            senha.toCharArray(),
            salt,
            ITERATIONS,
            KEY_SIZE
        );

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
