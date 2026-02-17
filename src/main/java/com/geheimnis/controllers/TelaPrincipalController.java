package com.geheimnis.controllers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geheimnis.metodosCriptografia.AESCTR;
import com.geheimnis.views.TelaProgresso;

public class TelaPrincipalController {

    private AESCTR crypt = new AESCTR();
    
    public void selecionarArquivo(JLabel path_arquivo_lbl ){
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        path_arquivo_lbl.setText(file.getAbsolutePath());
    }

    public void criptografarArquivo(JLabel path_arquivo_lbl, JRadioButton cript_rbtn, JTextField chave_in){
        final TelaProgresso progresso = new TelaProgresso(100);
        File arquivo = new File( path_arquivo_lbl.getText() );
        String chave = chave_in.getText();
        boolean criptografar = cript_rbtn.isSelected();
        if (! arquivo.exists()) 
            JOptionPane.showMessageDialog(null, "não foi achado um arquivo com o caminho:\n"+path_arquivo_lbl.getText(), "Erro", JOptionPane.ERROR_MESSAGE);

        progresso.setProgresso(0);
        Thread processarDados = new Thread(() -> {
            try{
                if (criptografar)
                    crypt.encrypt(arquivo, chave, progresso);
                else {
                    if (!arquivo.getAbsolutePath().endsWith(".cripto")){
                        JOptionPane.showMessageDialog(null, "este nao é um arquivo criptografado", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    crypt.decript(arquivo, chave, progresso);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "falha ao (des)criptografar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                progresso.close();
            }
            JOptionPane.showMessageDialog(null, "arquivo (des)criptografado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });
        processarDados.start();
    }

}
