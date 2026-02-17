package com.geheimnis.auxiliares;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;

public class GerenciadorLookAndFeel {
    
    public static void definirLookAndFeel(){
        try{
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {}
    }

    public static void definirIcone(JFrame frame, URL image){
        Image icon = Toolkit.getDefaultToolkit().getImage(image);
        frame.setIconImage(icon);
    }

}
