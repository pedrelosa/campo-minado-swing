
package br.com.pedrelosa.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private final int x;
	private final int y;
	private boolean minado;
	private boolean aberto;
	private boolean marcado;
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	
	public Campo(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	public void registrarObservador(CampoObservador observador){
		this.observadores.add(observador);
	}
	
	public void notificarObservadores(CampoEvento evento){
		this.observadores
				.forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	public boolean adicionarVizinho(Campo vizinho) {
		boolean xIsDif = (this.x != vizinho.x);
		boolean yIsDif = (this.y != vizinho.y);
		boolean diagonalIsDif = (xIsDif && yIsDif);
		
		int deltaX = Math.abs(this.x - vizinho.x);
		int deltaY = Math.abs(this.y - vizinho.y);
		int deltaAll = deltaX + deltaY;
		
		if (deltaAll == 1 && !diagonalIsDif) {
			this.vizinhos.add(vizinho);
			return true;
		}
		if (deltaAll == 2 && diagonalIsDif) {
			this.vizinhos.add(vizinho);
			return true;
			
		}
		return false;
	}
	
	
	public void alternarMarcacao() {
		if (! this.aberto) {
			this.marcado = ! this.marcado;
			
			if (isMinado()){
				notificarObservadores(CampoEvento.MARCAR);
			}else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
			
		}
	}
	
	
	public void abrir() {
		if (! this.isAberto() && ! this.isMarcado()) {
			
			if (this.isMinado()) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return;
			}
			
			setAberto(true);
			
			if (vizinhancaSegura()) {
				this.vizinhos.forEach(Campo::abrir);
				
			}
			
		}
	}
	
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean vizinhancaSegura() {
		return this.vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	public boolean isMarcado() {
		return this.marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if (isAberto()){
			notificarObservadores(CampoEvento.ABRIR);
		}
		
	}
	
	public boolean isAberto() {
		return this.aberto;
	}
	
	public boolean isMinado() {
		return this.minado;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = (! this.minado && this.aberto);
		boolean protegido = (this.minado && this.marcado);
		return (desvendado || protegido);
	}
	
	void minar() {
		if (! isMinado()) {
			this.minado = true;
		}
	}
	
	public int minasNaVizinhanca() {
		return (int) this.vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		this.aberto = false;
		this.minado = false;
		this.aberto = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
}
