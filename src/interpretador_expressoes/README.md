# Interpretador de Expressões Matemáticas

## Descrição da atividade

Esta atividade tem como objetivo implementar uma aplicação interativa em Java que utiliza uma estrutura de árvore para interpretar expressões matemáticas informadas pelo usuário. A aplicação recebe uma expressão em notação infixa, converte para notação pós-fixa, constrói a árvore de expressão e permite realizar operações como cálculo do resultado, visualização das notações e geração de um grafo Mermaid para análise da árvore.

A implementação foi desenvolvida como parte da disciplina de Estruturas de Dados e Algoritmos, utilizando conceitos de árvore binária, pilha e conversão de expressões.

## Objetivo

O sistema deve permitir ao usuário:

- inserir uma expressão matemática;
- converter a expressão da forma infixa para pós-fixa;
- construir a árvore binária correspondente;
- calcular o valor da expressão;
- exibir as notações infixa, prefixa e pós-fixa;
- gerar um link para visualizar a árvore em Mermaid.

## Estruturas utilizadas

- Árvore binária
- Pilha
- Nó da árvore
- Conversão de expressão infixa para pós-fixa

## Menu da aplicação

Ao executar o programa, o usuário visualiza o seguinte menu:

1. Inserir nova expressão matemática
2. Calcular resultado
3. Exibir notações (In-fixa, Pós-fixa e Pré-fixa)
4. Visualizar Árvore (Gerar link dinâmico Mermaid)
5. Sair

## Funcionamento

### 1. Inserir expressão
O usuário informa uma expressão matemática, como por exemplo:

- `2+3*4`
- `(5-9)/2`
- `2*3+5-10*4+8`

A aplicação sanitiza a expressão, converte para pós-fixa e monta a árvore binária correspondente.

### 2. Calcular resultado
A árvore é percorrida recursivamente para avaliar a expressão e retornar o valor numérico final.

### 3. Exibir notações
A aplicação exibe:

- In-fixa
- Pós-fixa
- Pré-fixa

### 4. Gerar árvore em Mermaid
A aplicação gera uma representação em Mermaid e disponibiliza um link para visualização online em:

- `https://mermaid.live/`

Assim, o usuário pode observar graficamente a estrutura da árvore gerada pela expressão.

## Exemplos de uso

### Exemplo 1
Entrada:

```text
2+3*4
```

Saída esperada:

- pós-fixa: `2 3 4 * +`
- resultado: `14`

### Exemplo 2
Entrada:

```text
(5-9)/2
```

Saída esperada:

- pós-fixa: `5 9 - 2 /`
- resultado: `-2`

### Exemplo 3
Entrada:

```text
2*3+5-10*4+8
```

Saída esperada:

- resultado: `-21`

## Validações implementadas

A aplicação trata corretamente alguns erros comuns, como:

- expressão vazia;
- operadores incompletos;
- parênteses desbalanceados;
- divisão por zero;
- token inválido.

## Como executar

No terminal, no diretório raiz do projeto, execute:

```powershell
cd "C:\Users\andre\OneDrive\Documentos\GitHub\ednl_algoritmos_20262"
& "C:\Program Files\Android\Android Studio\jbr\bin\javac.exe" src/interpretador_expressoes/*.java
& "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" -cp src interpretador_expressoes.Main
```

## Observações

Este projeto foi estruturado para servir como exemplo prático de uso de árvore binária em algoritmos de interpretação de expressões. O código pode ser expandido para incluir funções matemáticas, potência, raízes e outras operações adicionais.

## Conclusão

A aplicação desenvolvida é um interpretador interativo de expressões matemáticas que utiliza árvore binária para organizar a expressão e realizar seus cálculos. Ela demonstra de forma prática o uso de estruturas de dados e a conversão entre diferentes notações matemáticas.
