package br.com.pedrelosa.campoMinado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador {
	private final int linhas;
	private final int colunas;
	private final int minas;
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		
		
		sortearMinas(0, 0);
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(ResultadoEvento resultado) {
		observadores.forEach(o -> o.accept(resultado));
	}
	
	public void abrir(int linha, int coluna) {
		this.campos.parallelStream()
				.filter(c -> (c.getX() == linha && c.getY() == coluna))
				.findFirst()
				.ifPresent(Campo::abrir);
	}
	
	public void primeiroAbrir(int linha, int coluna) {
		sortearMinas(linha, coluna);
		abrir(linha, coluna);
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		this.campos.parallelStream()
				.filter(c -> (c.getX() == linha && c.getY() == coluna))
				.findFirst()
				.ifPresent(Campo::alternarMarcacao);
	}
	
	private void gerarCampos() {
		for (int linha = 0; linha < this.linhas; linha++) {
			for (int coluna = 0; coluna < this.colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				this.campos.add(campo);
			}
		}
	}
	
	private void associarVizinhos() {
		for (Campo c1 : this.campos) {
			for (Campo c2 : this.campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	private void sortearMinas(int linha, int coluna) {
		
		for (long minasArmadas = 0L; minasArmadas < this.minas; minasArmadas++) {
			int aleatorio = (int) (Math.random() * this.campos.size());
			if (this.campos.get(aleatorio).getX() != linha && this.campos.get(aleatorio).getY() != coluna) {
				this.campos.get(aleatorio).minar();
			}
			minasArmadas = this.campos.stream().filter(Campo::isMinado).count();
		}
		
	}
	
	public boolean objetivoAlcancado() {
		return this.campos.stream().allMatch(Campo::objetivoAlcancado);
	}
	
	public void reiniciar() {
		this.campos.forEach(Campo::reiniciar);
	}
	
	public int getLinhas() {
		return this.linhas;
	}
	
	public int getColunas() {
		return this.colunas;
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(new ResultadoEvento(false));
		} else if (objetivoAlcancado()) {
			notificarObservadores(new ResultadoEvento(true));
		}
	}
	
	private void mostrarMinas() {
		this.campos.stream()
				.filter(Campo::isMinado)
				.filter(c -> !c.isMarcado())
				.forEach(c -> c.setAberto(true));
	}
}
