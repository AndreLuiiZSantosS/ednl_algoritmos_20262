package interpretador_expressoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConversorExpressao {

	// prepara a expressao para leitura por tokens
	public String sanitizarExpressao(String input) {
		if(input == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < input.length(); i++) {
			char caractere = input.charAt(i);

			if(ehOperadorOuParentese(caractere)) {
				sb.append(' ').append(caractere).append(' ');
			}else {
				sb.append(caractere);
			}
		}

		return sb.toString().trim().replaceAll("\\s+", " ");
	}

	// converte infixa para pos-fixa
	public String converterParaPosFixa(String expressaoInfixa) {
		String expressao = this.sanitizarExpressao(expressaoInfixa);

		if(expressao.isEmpty()) {
			throw new IllegalArgumentException("A expressao nao pode estar vazia.");
		}

		String[] tokens = expressao.split(" ");
		List<String> saida = new ArrayList<String>();
		Stack<String> operadores = new Stack<String>();
		boolean esperandoOperando = true;

		for(int i = 0; i < tokens.length; i++) {
			String token = tokens[i];

			if(token.isEmpty()) {
				continue;
			}

			if(ehNumero(token)) {
				saida.add(token);
				esperandoOperando = false;
			}else if("(".equals(token)) {
				operadores.push(token);
				esperandoOperando = true;
			}else if(")".equals(token)) {
				if(esperandoOperando) {
					throw new IllegalArgumentException("Parenteses vazio ou expressao incompleta.");
				}

				desempilharAteParentese(operadores, saida);
				esperandoOperando = false;
			}else if(ehOperador(token)) {
				if(esperandoOperando) {
					i = tratarSinal(tokens, i, operadores, saida);
					esperandoOperando = !tokenAtualEhNumero(tokens, i);
				}else {
					desempilharPorPrecedencia(token, operadores, saida);
					operadores.push(token);
					esperandoOperando = true;
				}
			}else {
				throw new IllegalArgumentException("Token invalido encontrado: " + token);
			}
		}

		if(esperandoOperando) {
			throw new IllegalArgumentException("A expressao terminou com um operador.");
		}

		while(!operadores.isEmpty()) {
			String operador = operadores.pop();

			if("(".equals(operador) || ")".equals(operador)) {
				throw new IllegalArgumentException("Parenteses desbalanceados.");
			}

			saida.add(operador);
		}

		return expandirSinais(saida);
	}

	// trata + e - como sinal antes de um operando
	private int tratarSinal(String[] tokens, int indice, Stack<String> operadores, List<String> saida) {
		String token = tokens[indice];

		if(!"+".equals(token) && !"-".equals(token)) {
			throw new IllegalArgumentException("Operador em posicao invalida: " + token);
		}

		if(indice + 1 >= tokens.length) {
			throw new IllegalArgumentException("Operador sem operando.");
		}

		String proximo = tokens[indice + 1];

		if(ehNumero(proximo)) {
			if("-".equals(token)) {
				saida.add("-" + proximo);
			}else {
				saida.add(proximo);
			}

			return indice + 1;
		}

		if("-".equals(token)) {
			operadores.push("u-");
		}

		return indice;
	}

	private void desempilharAteParentese(Stack<String> operadores, List<String> saida) {
		while(!operadores.isEmpty() && !"(".equals(operadores.peek())) {
			saida.add(operadores.pop());
		}

		if(operadores.isEmpty()) {
			throw new IllegalArgumentException("Parenteses desbalanceados.");
		}

		operadores.pop();
	}

	private void desempilharPorPrecedencia(String operador, Stack<String> operadores, List<String> saida) {
		while(!operadores.isEmpty()
				&& ehOperador(operadores.peek())
				&& precedencia(operadores.peek()) >= precedencia(operador)) {
			saida.add(operadores.pop());
		}
	}

	// transforma sinal unario em subtracao binaria
	private String expandirSinais(List<String> tokens) {
		Stack<List<String>> pilha = new Stack<List<String>>();

		for(String token : tokens) {
			if(ehNumero(token)) {
				List<String> numero = new ArrayList<String>();
				numero.add(token);
				pilha.push(numero);
			}else if("u-".equals(token)) {
				if(pilha.isEmpty()) {
					throw new IllegalArgumentException("Operador sem operando.");
				}

				List<String> operando = pilha.pop();
				List<String> expressao = new ArrayList<String>();
				expressao.add("0");
				expressao.addAll(operando);
				expressao.add("-");
				pilha.push(expressao);
			}else if(ehOperador(token)) {
				if(pilha.size() < 2) {
					throw new IllegalArgumentException("Expressao pos-fixa invalida.");
				}

				List<String> direita = pilha.pop();
				List<String> esquerda = pilha.pop();
				List<String> expressao = new ArrayList<String>();
				expressao.addAll(esquerda);
				expressao.addAll(direita);
				expressao.add(token);
				pilha.push(expressao);
			}else {
				throw new IllegalArgumentException("Token invalido encontrado: " + token);
			}
		}

		if(pilha.size() != 1) {
			throw new IllegalArgumentException("Expressao pos-fixa invalida.");
		}

		return String.join(" ", pilha.pop());
	}

	private int precedencia(String operador) {
		if("u-".equals(operador)) {
			return 3;
		}

		if("*".equals(operador) || "/".equals(operador)) {
			return 2;
		}

		if("+".equals(operador) || "-".equals(operador)) {
			return 1;
		}

		return 0;
	}

	private boolean tokenAtualEhNumero(String[] tokens, int indice) {
		return indice >= 0 && indice < tokens.length && ehNumero(tokens[indice]);
	}

	private boolean ehNumero(String token) {
		return token.matches("[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+))");
	}

	private boolean ehOperador(String token) {
		return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token) || "u-".equals(token);
	}

	private boolean ehOperadorOuParentese(char caractere) {
		return caractere == '+'
				|| caractere == '-'
				|| caractere == '*'
				|| caractere == '/'
				|| caractere == '('
				|| caractere == ')';
	}
}
