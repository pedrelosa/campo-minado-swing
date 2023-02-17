package br.com.pedrelosa.campoMinado.visao;

import br.com.pedrelosa.campoMinado.modelo.Campo;
import br.com.pedrelosa.campoMinado.modelo.CampoEvento;
import br.com.pedrelosa.campoMinado.modelo.CampoObservador;
import br.com.pedrelosa.campoMinado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton
		implements CampoObservador, MouseListener {
	
	private final Color BG_PADRAO = new Color(184,184,184);
	private final Color BG_MARCAR = new Color(8,179,247);
	private final Color BG_EXPLODIR = new Color(189,66,68);
	private final Color TEXTO_VERTE = new Color(0,100,0);
	
	private Campo campo;
	
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		
		aplicarEstiloPadrao();
		
		addMouseListener(this);
		
		campo.registrarObservador(this);
		
	
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
			case ABRIR -> aplicarEstiloAbrir();
			case MARCAR -> aplicarEstiloMarcar();
			case DESMARCAR -> aplicarEstiloDesmarcar();
			case EXPLODIR -> aplicarEstiloExplodir();
			default -> aplicarEstiloPadrao();
		}
	}
	
	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setFocusable(false);
		setOpaque(true);
		setFont(new Font("arial", Font.BOLD, 20));
		setText("");
	}
	
	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setText("X");
	}
	
	private void aplicarEstiloDesmarcar() {
		aplicarEstiloPadrao();
	}
	
	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setText("M");
	}
	
	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if (campo.isMinado()){
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(BG_PADRAO);
		switch (campo.minasNaVizinhanca()) {
			case 1 -> setForeground(TEXTO_VERTE);
			case 2 -> setForeground(TEXTO_VERTE);
			case 3 -> setForeground(TEXTO_VERTE);
			case 4, 5, 6 -> setForeground(TEXTO_VERTE);
			default -> setForeground(Color.PINK);
		}
	
		String valor = !campo.vizinhancaSegura() ?
					campo.minasNaVizinhanca() + "" : "";
		
		setText(valor);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1){
			System.out.println("usou botão direito");
			campo.abrir();
		} else if (e.getButton() == 3) {
			System.out.println("usou botão esquerdo");
			campo.alternarMarcacao();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
}
