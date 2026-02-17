package com.geheimnis.controllers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geheimnis.metodosCriptografia.AESGCM;

public class TelaPrincipalController {

    private AESGCM crypt = new AESGCM();
    
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
        if (! arquivo.exists()) 
            JOptionPane.showMessageDialog(null, "não foi achado um arquivo com o caminho:\n"+path_arquivo_lbl.getText(), "Erro", JOptionPane.ERROR_MESSAGE);

        try{
            if (criptografar)
                crypt.encrypt(arquivo, chave);
            else {
                if (!arquivo.getAbsolutePath().endsWith(".cripto")){
                    JOptionPane.showMessageDialog(null, "este nao é um arquivo criptografado", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                crypt.decript(arquivo, chave);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "falha ao (des)criptografar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        
        JOptionPane.showMessageDialog(null, "arquivo (des)criptografado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

}
