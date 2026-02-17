package com.geheimnis.views;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geheimnis.controllers.TelaPrincipalController;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TelaPrincipal {
    
    private final JFrame TELA = new JFrame();
    private final String TITULO = "CRYPTO";
    private final int W = 400;
    private final int H = 200;
    private final int PADDING = 15;

    private final TelaPrincipalController controller = new TelaPrincipalController();

    //________METODOS PUBLICOS
    public void show(){
        TELA.setTitle(TITULO);
        TELA.setSize(new Dimension(W, H));
        construirInterface(TELA);
        TELA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        TELA.setVisible(true);
    }

    public JFrame getJFrame(){
        return TELA;
    }


    //_________METODOS PRIVADOS
    private void construirInterface(JFrame tela){
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        JButton escolhedorArquivo_btn = new JButton("arquivo");
        JRadioButton cript_rbtn = new JRadioButton("criptografar");
        JRadioButton decript_rbtn = new JRadioButton("descriptografar");
        JLabel path_arquivo_lbl = new JLabel("selecione um arquivo");
        JLabel chave_lbl = new JLabel("Chave:");
        JTextField chave_in = new JTextField();
        JButton pronto_btn = new JButton("pronto");
        
        // layout 4 x 3 ( 0-3 x 0-2 )
        gbc.insets = new Insets(0, 0, 5, 5);
        //0, 0 - botao de escolher arquivo + nome do arquivo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        painel.add(escolhedorArquivo_btn, gbc);
        gbc.insets = new Insets(0, 0, 5, 0);
        //0, 1 - caminho do arquivo escolhido
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        painel.add(path_arquivo_lbl, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        //1, 0 - radio button
        ButtonGroup grupoRadios = new ButtonGroup();
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(cript_rbtn, gbc);
        cript_rbtn.setSelected(true);
        //2, 0 - radio button
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(decript_rbtn, gbc);
        grupoRadios.add(cript_rbtn);
        grupoRadios.add(decript_rbtn);
        //3, 0 - label
        gbc.gridx = 0;
        gbc.gridy = 3;
        painel.add(chave_lbl, gbc);
        //3, 1 - textfield da chave
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(chave_in, gbc);
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        //3, 2 - botao de pronto
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 5, 0, 0);
        painel.add(pronto_btn, gbc);

        //definindo eventos
        escolhedorArquivo_btn.addActionListener(e -> { controller.selecionarArquivo(path_arquivo_lbl); });
        pronto_btn.addActionListener(e -> { controller.criptografarArquivo(path_arquivo_lbl, cript_rbtn, chave_in); });

        tela.add(painel);
    }
}
