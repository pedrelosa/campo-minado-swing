package br.com.pedrelosa.campoMinado.visao;

import br.com.pedrelosa.campoMinado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
	
	public TelaPrincipal(){
		Tabuleiro tabuleiro = new Tabuleiro(16,30,30);
		
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado");
		setSize(675,438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
	}
	
}
