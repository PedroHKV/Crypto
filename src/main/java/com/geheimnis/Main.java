package com.geheimnis;

import java.net.URL;

import com.geheimnis.configs.GerenciadorLookAndFeel;
import com.geheimnis.views.TelaPrincipal;

public class Main {

    private static final TelaPrincipal tela = new TelaPrincipal();
    private static final URL icone = Main.class.getResource("/imgs/icone.png");
    public static void main(String[] args) {
        GerenciadorLookAndFeel.definirLookAndFeel();
        GerenciadorLookAndFeel.definirIcone(tela.getJFrame(), icone);
        tela.show();
    }
}
