package interpretador_expressoes;

public class No {
	String valor;
	No esquerda;
	No direita;

	// construtor padrao
	public No() {
		this.valor = "";
		this.esquerda = null;
		this.direita = null;
	}

	// construtor com valor
	public No(String valor) {
		this.valor = valor;
		this.esquerda = null;
		this.direita = null;
	}
}
