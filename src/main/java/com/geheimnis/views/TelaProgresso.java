package com.geheimnis.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class TelaProgresso {
    
    private final String TITULO = "Progresso";
    private final int W = 400;
    private final int H = 100;
    
    private JFrame frame;
    private JProgressBar barra;

    public TelaProgresso(int max){
        frame = new JFrame();
        barra = new JProgressBar();
        barra.setMaximum(max);
    }

    public void show(){
        frame.setTitle(TITULO);
        frame.setSize(new Dimension(W, H));
        construirInterface(frame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void close(){
        frame.dispose();
    }

    public void setMaximum(int v){
        barra.setMaximum(v);
    }

    public void setProgresso(int v){
        SwingUtilities.invokeLater(() -> barra.setValue(v));
    }


    //______METODOS PRIVADOS

    private void construirInterface(JFrame frame){
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel descricao = new JLabel("processando dados");
        barra.setForeground(new Color(100,100,250));

        //label acima 
        gbc.insets = new Insets(0, 15, 15, 15);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridy = 0;
        frame.add(descricao, gbc);
        //barra abaixo
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(barra, gbc);
    }
}
