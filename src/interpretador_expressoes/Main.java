package interpretador_expressoes;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ConversorExpressao conversor = new ConversorExpressao();
		ArvoreExpressao arvore = new ArvoreExpressao();

		while(true) {
			exibirMenu();
			String opcao = scanner.nextLine().trim();

			if("1".equals(opcao)) {
				System.out.println("Digite a expressão matemática completa e pressione Enter:");
				String entrada = scanner.nextLine();

				try {
					String expressaoPosFixa = conversor.converterParaPosFixa(entrada);
					arvore = new ArvoreExpressao();
					arvore.construirArvore(expressaoPosFixa);

					System.out.println("Expressão carregada com sucesso!");
					System.out.println("Pós-fixa: " + arvore.imprimirPosOrdem());
					imprimirEquacao(arvore);
				}catch(IllegalArgumentException e) {
					System.out.println("Erro ao processar a expressão: " + e.getMessage());
				}
			}else if("2".equals(opcao)) {
				if(arvore.arvoreVazia()) {
					System.out.println("Nenhuma expressão foi inserida ainda.");
					continue;
				}

				try {
					double resultado = arvore.calcularResultado(arvore.obterRaiz());
					System.out.println("Resultado: " + formatarNumero(resultado));
				}catch(ArithmeticException e) {
					System.out.println("Erro no cálculo: " + e.getMessage());
				}
			}else if("3".equals(opcao)) {
				if(arvore.arvoreVazia()) {
					System.out.println("Nenhuma expressão foi inserida ainda.");
					continue;
				}

				imprimirEquacao(arvore);
				System.out.println("Pós-fixa: " + arvore.imprimirPosOrdem());
				System.out.println("Pré-fixa: " + arvore.imprimirPreOrdem());
			}else if("4".equals(opcao)) {
				if(arvore.arvoreVazia()) {
					System.out.println("Nenhuma expressão foi inserida ainda.");
					continue;
				}

				System.out.println("Link Mermaid Live:");
				try {
					arvore.gerarLinkMermaid();
				}catch(ArithmeticException e) {
					System.out.println("Erro no cálculo: " + e.getMessage());
				}
			}else if("5".equals(opcao)) {
				System.out.println("Encerrando o interpretador.");
				scanner.close();
				return;
			}else {
				System.out.println("Opção inválida. Escolha uma opção de 1 a 5.");
			}
		}
	}

	private static void exibirMenu() {
		System.out.println();
		System.out.println("--- INTERPRETADOR DE EXPRESSÕES ---");
		System.out.println("1. Inserir nova expressão matemática");
		System.out.println("2. Calcular resultado");
		System.out.println("3. Exibir notações (In-fixa, Pós-fixa e Pré-fixa)");
		System.out.println("4. Visualizar Árvore (Gerar link dinâmico Mermaid)");
		System.out.println("5. Sair");
		System.out.print("Escolha uma opção: ");
	}

	private static void imprimirEquacao(ArvoreExpressao arvore) {
		try {
			System.out.println("In-fixa: " + arvore.imprimirInOrdem());
		}catch(ArithmeticException e) {
			System.out.println("In-fixa: " + arvore.imprimirInOrdem());
			System.out.println("Erro no cálculo: " + e.getMessage());
		}
	}

	private static String formatarNumero(double valor) {
		if(valor == Math.rint(valor)) {
			return String.format("%.0f", valor);
		}

		return Double.toString(valor);
	}
}
