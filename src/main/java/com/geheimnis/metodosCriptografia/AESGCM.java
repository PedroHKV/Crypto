package com.geheimnis.metodosCriptografia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

//salva num arquivo .cript ou descriptografa pra txt
//1- IV
//2- saltos
//3- texto cifrado
public class AESGCM{

    private static final int TAMANHO_CHAVES_GERADAS = 128;
    private static final int TAMANHO_IV = 12;
    private static final int TAMANHO_SALTOS = 16;
    private static final int BUFFER = 1024 * 10; // 10 MB
    private static final int TAMANHO_TAG = 128;

    public void encrypt(File arquivo, String senha) throws Exception {
        //declarações necessárias
        byte[] buffer_leitura = new byte[BUFFER];
        FileInputStream Fin = new FileInputStream(arquivo);
        FileOutputStream Fout = new FileOutputStream(new File(arquivo.getAbsolutePath()+".cripto"), true);
        //1- gera saltos aleatorios
        byte[] saltos = gerarSaltos();
        //2- gera uma senha mais forte com saltos aleatorios
        SecretKey key = gerarKey(senha, saltos);
        //3- gera um vetor de inicialização aleatorio
        byte[] IV = gerarIVAleatorio();
        //4- cria o Cipher
        Cipher cipher = getCipherConfigurado(IV, key, Cipher.ENCRYPT_MODE);
        //5- grava os dados do IV
        Fout.write(IV);
        //6- grava os dados dos saltos
        Fout.write(saltos);
        //inicia o loop de encriptação
        int lidos;
        while ((lidos = Fin.read(buffer_leitura)) != -1) {
            byte[] parcial = cipher.update(buffer_leitura, 0, lidos);
            if (parcial != null) {
                Fout.write(parcial);
            }
        }
        Fout.write(cipher.doFinal());
        Fin.close();
        Fout.close();
    }

    public void decript(File arquivo, String senha) throws Exception {
        byte[] buffer_leitura = new byte[BUFFER];
        byte[] saltos = new byte[TAMANHO_SALTOS];
        FileInputStream Fin = new FileInputStream(arquivo);
        FileOutputStream Fout = new FileOutputStream(new File(arquivo.getAbsolutePath().replaceAll(".cripto", "" )), true);
        //1- restaura o vetor de inicialização do arquivo gravado nos primeiros 'TAMANHO_IV' dados do arquivo
        byte[] IV = new byte[TAMANHO_IV];
        int total = 0;
        while (total < TAMANHO_IV) {
            int r = Fin.read(IV, total, TAMANHO_IV - total);
            if (r == -1) {
                Fin.close();
                Fout.close();
                throw new IOException("Arquivo corrompido (IV incompleto)");
            }
            total += r;
        }
        //2- restaura os saltos gravados no arquivo
        total = 0;
        while (total < TAMANHO_SALTOS) {
            int r = Fin.read(saltos, total, TAMANHO_SALTOS - total);
            if (r == -1) {
                Fin.close();
                Fout.close();
                throw new IOException("Arquivo corrompido (Saltos incompletos)");
            }
            total += r;
        }
        //3- gera a mesma SecretKey com base na palavra chave
        SecretKey key = gerarKey(senha, saltos );
        //4- cria o Cipher
        Cipher cipher = getCipherConfigurado(IV, key, Cipher.DECRYPT_MODE);
        //inicia o loop de decriptação, com uma pequena garantia de que o TAG não seja mal interpretado
        int lidos;
        while ((lidos = Fin.read(buffer_leitura)) != -1) {
            byte[] parcial = cipher.update(buffer_leitura, 0, lidos);
            if (parcial != null) {
                Fout.write(parcial);
            }
        }
        Fout.write(cipher.doFinal());
        Fin.close();
        Fout.close();
    }

    //__________METODOS PRIVADOS__________
    
    //gera uma chave mais forte com base na senha
    private SecretKey gerarKey(String senha, byte[] saltos) throws Exception{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(senha.toCharArray(), saltos, 65536, TAMANHO_CHAVES_GERADAS);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    //gera um vetor de inicialização aleatorio
    private byte[] gerarIVAleatorio(){
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[TAMANHO_IV];
        random.nextBytes(iv);
        return iv;
    }
    //gera saltos aleatorios
    private byte[] gerarSaltos(){
        SecureRandom random = new SecureRandom();
        byte[] saltos = new byte[TAMANHO_SALTOS];
        random.nextBytes(saltos);
        return saltos;
    }

    private Cipher getCipherConfigurado(byte[] iv, SecretKey key, int cipherMode) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAMANHO_TAG, iv);
        cipher.init(cipherMode, key, spec);
        return cipher;
    }
   
}
