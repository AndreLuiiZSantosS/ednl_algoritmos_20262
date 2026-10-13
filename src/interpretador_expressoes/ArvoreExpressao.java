package interpretador_expressoes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Stack;

public class ArvoreExpressao {
	No raiz;

	// construtor de arvore vazia
	public ArvoreExpressao() {
		this.raiz = null;
	}

	// verifica se a arvore esta vazia
	public boolean arvoreVazia() {
		if(this.raiz == null) {
			return true;
		}

		return false;
	}

	// retorna a raiz da arvore
	public No obterRaiz() {
		return this.raiz;
	}

	// constroi a arvore a partir da pos-fixa
	public void construirArvore(String expressaoPosFixa) {
		if(expressaoPosFixa == null || expressaoPosFixa.trim().isEmpty()) {
			throw new IllegalArgumentException("A expressao pos-fixa nao pode estar vazia.");
		}

		Stack<No> pilha = new Stack<No>();
		String[] tokens = expressaoPosFixa.trim().split("\\s+");

		for(String token : tokens) {
			if(ehNumero(token)) {
				pilha.push(new No(token));
			}else if(ehOperador(token)) {
				if(pilha.size() < 2) {
					throw new IllegalArgumentException("Expressao pos-fixa invalida.");
				}

				No direita = pilha.pop();
				No esquerda = pilha.pop();
				No operador = new No(token);

				operador.esquerda = esquerda;
				operador.direita = direita;
				pilha.push(operador);
			}else {
				throw new IllegalArgumentException("Token invalido encontrado: " + token);
			}
		}

		if(pilha.size() != 1) {
			throw new IllegalArgumentException("Expressao pos-fixa invalida.");
		}

		this.raiz = pilha.pop();
	}

	// calcula a expressao em pos-ordem
	public double calcularResultado(No no) {
		if(no == null) {
			throw new IllegalArgumentException("Nao ha arvore para calcular.");
		}

		if(no.esquerda == null && no.direita == null) {
			return Double.parseDouble(no.valor);
		}

		double valorEsquerda = calcularResultado(no.esquerda);
		double valorDireita = calcularResultado(no.direita);

		if("+".equals(no.valor)) {
			return valorEsquerda + valorDireita;
		}

		if("-".equals(no.valor)) {
			return valorEsquerda - valorDireita;
		}

		if("*".equals(no.valor)) {
			return valorEsquerda * valorDireita;
		}

		if("/".equals(no.valor)) {
			if(valorDireita == 0.0) {
				throw new ArithmeticException("Divisao por zero.");
			}

			return valorEsquerda / valorDireita;
		}

		throw new IllegalArgumentException("Operador desconhecido: " + no.valor);
	}

	// imprime em ordem com o resultado como igualdade
	public String imprimirInOrdem() {
		if(this.arvoreVazia()) {
			return "Arvore vazia!";
		}

		String expressao = percorrerInOrdem(this.raiz);
		double resultado = calcularResultado(this.raiz);
		return expressao + " = " + formatarNumero(resultado);
	}

	// imprime em pre-ordem
	public String imprimirPreOrdem() {
		if(this.arvoreVazia()) {
			return "Arvore vazia!";
		}

		return percorrerPreOrdem(this.raiz).trim();
	}

	// imprime em pos-ordem
	public String imprimirPosOrdem() {
		if(this.arvoreVazia()) {
			return "Arvore vazia!";
		}

		return percorrerPosOrdem(this.raiz).trim();
	}

	// gera o link do Mermaid Live com a estrutura: resultado = expressao
	public String gerarLinkMermaid() {
		if(this.arvoreVazia()) {
			System.out.println("Arvore vazia!");
			return "";
		}

		No raizVisualizacao = criarRaizIgualdade();
		StringBuilder mermaid = new StringBuilder();
		mermaid.append("graph TD\n");
		gerarMermaid(raizVisualizacao, mermaid);

		String json = "{\"code\":\"" + escaparJson(mermaid.toString())
				+ "\",\"mermaid\":{\"theme\":\"default\"},\"autoSync\":true,\"updateDiagram\":true}";
		String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
		String url = "https://mermaid.live/view#base64:" + base64;

		System.out.println(url);
		return url;
	}

	private No criarRaizIgualdade() {
		No raizIgualdade = new No("=");
		No resultado = new No(formatarNumero(calcularResultado(this.raiz)));
		raizIgualdade.esquerda = resultado;
		raizIgualdade.direita = this.raiz;
		return raizIgualdade;
	}

	private String percorrerInOrdem(No no) {
		if(no == null) {
			return "";
		}

		if(no.esquerda == null && no.direita == null) {
			return no.valor;
		}

		return "( " + percorrerInOrdem(no.esquerda) + " " + no.valor + " " + percorrerInOrdem(no.direita) + " )";
	}

	private String percorrerPreOrdem(No no) {
		if(no == null) {
			return "";
		}

		return no.valor + " " + percorrerPreOrdem(no.esquerda) + percorrerPreOrdem(no.direita);
	}

	private String percorrerPosOrdem(No no) {
		if(no == null) {
			return "";
		}

		return percorrerPosOrdem(no.esquerda) + percorrerPosOrdem(no.direita) + no.valor + " ";
	}

	private void gerarMermaid(No no, StringBuilder sb) {
		if(no == null) {
			return;
		}

		String idAtual = gerarId(no);
		sb.append(idAtual).append("[\"").append(escaparMermaid(no.valor)).append("\"]\n");

		if(no.esquerda != null) {
			String idEsquerda = gerarId(no.esquerda);
			sb.append(idAtual).append(" --> ").append(idEsquerda).append("\n");
			gerarMermaid(no.esquerda, sb);
		}

		if(no.direita != null) {
			String idDireita = gerarId(no.direita);
			sb.append(idAtual).append(" --> ").append(idDireita).append("\n");
			gerarMermaid(no.direita, sb);
		}
	}

	private String gerarId(No no) {
		return "no" + Integer.toUnsignedString(System.identityHashCode(no));
	}

	private String formatarNumero(double valor) {
		if(valor == Math.rint(valor)) {
			return String.format("%.0f", valor);
		}

		return Double.toString(valor);
	}

	private String escaparJson(String texto) {
		return texto.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\r", "")
				.replace("\n", "\\n");
	}

	private String escaparMermaid(String texto) {
		return texto.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	private boolean ehNumero(String token) {
		return token.matches("[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+))");
	}

	private boolean ehOperador(String token) {
		return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
	}
}
