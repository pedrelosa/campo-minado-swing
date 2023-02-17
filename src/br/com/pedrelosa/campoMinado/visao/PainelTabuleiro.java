package br.com.pedrelosa.campoMinado.visao;

import br.com.pedrelosa.campoMinado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		tabuleiro.paraCadaCampo(c -> add(new  BotaoCampo(c)));
		
		tabuleiro.registrarObservador(e -> {
			SwingUtilities.invokeLater(() -> {
				int resposta;
				if (e.isGanhou()){
					resposta = JOptionPane.showConfirmDialog(this, "Você ganhou :)" +
							"\n deseja jogar novamente?");
					if (resposta == JOptionPane.YES_OPTION){
						tabuleiro.reiniciar();
					}else {
						System.exit(0);
					}
				}else {
					resposta = JOptionPane.showConfirmDialog(this, "Você perdeu :(" +
							"\n deseja jogar novamente?");
					if (resposta == JOptionPane.YES_OPTION){
						tabuleiro.reiniciar();
					}else {
						System.exit(0);
					}
				}
			});
		});
		
	}
}
