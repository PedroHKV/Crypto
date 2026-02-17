package com.geheimnis.metodosCriptografia;

import java.io.File;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.geheimnis.auxiliares.Arquivista;
import com.geheimnis.views.TelaProgresso;

//salva num arquivo .cript ou descriptografa pra txt
//1- IV
//2- saltos
//3- texto cifrado
public class AESCTR{

    private static final int TAMANHO_CHAVES_GERADAS = 128;
    private static final int TAMANHO_IV = 16; //para o CTR o IV = 16 é OBRIGATÓRIO
    private static final int TAMANHO_SALTOS = 16;
    private static final int BUFFER = 1024 * 64; // 64 KB
    private static final Arquivista arquivista = new Arquivista();

    public void encrypt(File arquivo, String senha, TelaProgresso tela) throws Exception {
        //configuração da tela de carregamento
        TelaProgresso progresso = new TelaProgresso(100);
        arquivista.setFin(arquivo);
        arquivista.setFout(new File(arquivo.getAbsolutePath()+".cripto"), true);
        //1- gera saltos aleatorios
        byte[] saltos = gerarSaltos();
        //2- gera uma senha mais forte com saltos aleatorios
        SecretKey key = gerarKey(senha, saltos);
        //3- gera um vetor de inicialização aleatorio
        byte[] IV = gerarIVAleatorio();
        //4- cria o Cipher
        Cipher cipher = getCipherConfigurado(IV, key, Cipher.ENCRYPT_MODE);
        //5- grava os dados do IV
        arquivista.write(IV);
        //6- grava os dados dos saltos
        arquivista.write(saltos);
        //inicia o loop de encriptação
        byte[] buffer_leitura = new byte[BUFFER];
        final long TAMANHO = arquivo.length();
        long lidos;
        long criptografados = 0;
        progresso.show();
        while ((lidos = arquivista.read(buffer_leitura)) != -1) {
            arquivista.write(cipher.update(buffer_leitura));
            criptografados += lidos;
            progresso.setProgresso(Integer.parseInt(Long.toString(100 * criptografados / TAMANHO)));
        }
        progresso.close();
        arquivista.write(cipher.doFinal());
        arquivista.close();
    }

    public void decript(File arquivo, String senha, TelaProgresso progresso) throws Exception {
        arquivista.setFin(arquivo);
        arquivista.setFout(new File(arquivo.getAbsolutePath().replaceAll(".cripto", "")), true);
        //1- restaura o vetor de inicialização do arquivo gravado nos primeiros 'TAMANHO_IV' dados do arquivo
        byte[] IV = new byte[TAMANHO_IV];
        arquivista.read(IV);
        //2- restaura os saltos gravados no arquivo
        byte[] saltos = new byte[TAMANHO_SALTOS];
        arquivista.read(saltos);
        //3- gera a mesma SecretKey com base na palavra chave
        SecretKey key = gerarKey(senha, saltos );
        //4- cria o Cipher
        Cipher cipher = getCipherConfigurado(IV, key, Cipher.DECRYPT_MODE);
        //inicia o loop de decriptação, com uma pequena garantia de que o TAG não seja mal interpretado
        int lidos;
        final long TAMANHO = arquivo.length();
        byte[] buffer_leitura = new byte[BUFFER];
        long descriptografados = 0;
        progresso.show();
        while ((lidos = arquivista.read(buffer_leitura)) != -1) {
            byte[] parcial = cipher.update(buffer_leitura, 0, lidos);
            arquivista.write(parcial);
            descriptografados += lidos;
            progresso.setProgresso(Integer.parseInt(Long.toString(100 * descriptografados / TAMANHO)));
            
        }
        arquivista.write(cipher.doFinal());
        arquivista.close();
    }

    public void metodoPosExcecao(){
        try{
            arquivista.close();
        } catch (NullPointerException e){
            // caso isso aconteça, significa que os fluxos foram fechados
        } catch (Exception e){
            // apenas para debug, o ideal é que esse caminho nunca aconteça
            System.err.println("vazamento de memoria, fluxos abertos");
        }
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
    //gera um objeto do tipo cipher já criptografado
    private Cipher getCipherConfigurado(byte[] iv, SecretKey key, int cipherMode) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec spec = new IvParameterSpec(iv);
        cipher.init(cipherMode, key, spec);
        return cipher;
    }

   
}
