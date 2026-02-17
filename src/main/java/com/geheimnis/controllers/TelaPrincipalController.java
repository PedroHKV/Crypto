package com.geheimnis.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geheimnis.abstracts.Criptografia;
import com.geheimnis.metodosCriptografia.AESGCM;

public class TelaPrincipalController {

    private Criptografia crypt = new AESGCM();
    private FileInputStream fin;
    private FileOutputStream fout;
    
    public void selecionarArquivo(JLabel path_arquivo_lbl ){
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        path_arquivo_lbl.setText(file.getAbsolutePath());
    }

    public void criptografarArquivo(JLabel path_arquivo_lbl, JRadioButton cript_rbtn, JTextField chave_in){
        File arquivo = new File( path_arquivo_lbl.getText() );
        String chave = chave_in.getText();
        boolean criptografar = cript_rbtn.isSelected();
        long tamanho = arquivo.length();
        if (! arquivo.exists()) 
            JOptionPane.showMessageDialog(null, "não foi achado um arquivo com o caminho:\n"+path_arquivo_lbl.getText(), "Erro", JOptionPane.ERROR_MESSAGE);
        if (tamanho > Integer.MAX_VALUE){
            JOptionPane.showMessageDialog(null, "o arquivo e muito grande para ser processado", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        byte[] dados = new byte[(int) tamanho];
        try{
            fin = new FileInputStream(arquivo);
            fin.read(dados);
            if(criptografar)
                dados = crypt.encrypt(dados, chave);
            else 
                dados = crypt.decript(dados, chave);
            fin.close();
            fout = new FileOutputStream(arquivo);
            fout.write(dados);
            fout.close();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "não foi possivel ler ou gravar os dados de:\n"+path_arquivo_lbl.getText(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } 
        JOptionPane.showMessageDialog(null, "arquivo (des)criptografado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

}
