package com.geheimnis.abstracts;

public interface Criptografia {
    public byte[] encrypt(byte[] data, String key) throws Exception ;
    public byte[] decript(byte[] data, String key) throws Exception ;
}
