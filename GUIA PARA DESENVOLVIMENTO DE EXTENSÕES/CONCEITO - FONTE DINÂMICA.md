# 📌 Conceitos Básicos sobre Fontes Dinâmicas
---
As fontes dinâmicas são uma das ferramentas disponíveis no Studio Extensões. Sua principal utilidade é atuar como um backend para o desenvolvimento de relatórios, sejam eles padrões ou customizados. Todo relatório depende de fontes dinâmicas para buscar informações no banco de dados utilizado.

# 🖥️ Características do BFC-SCRIPT
---
- Sintaxe similar ao Java.
- Suporte a operadores lógicos, comparação, atribuição e aritméticos.
- Uso de chaves {} para circundar blocos de código.
- Case-sensitive (diferencia letras maiúsculas e minúsculas).
- Não requer ponto e vírgula (;) ao final das instruções.
- Comandos em português, facilitando o uso por usuários sem experiência em programação.

# 📑 Estrutura Padrão de uma Fonte Dinâmica
---
A estrutura básica para criar uma fonte dinâmica é a seguinte:
```
// Define a estrutura dos dados que serão armazenados na fonte dinâmica
esquema = [
  id: Esquema.numero,    # Campo numérico para identificar cada registro
  nome: Esquema.caracter, # Campo de texto para armazenar o nome
  codigo: Esquema.caracter # Campo de texto para armazenar o código
]

// Cria a fonte dinâmica baseada no esquema definido acima
fonte = Dados.dinamico.v2.novo(esquema);

// Define a origem dos dados, que será a fonte de contribuintes
fonteMatriculas = Dados.folha.v2.matriculas;

// Obtém o valor do parâmetro selecionado para filtrar as matrículas
p_matricula = parametros.matricula.selecionado.valor

// Inicializa uma lista vazia para armazenar os registros antes de inseri-los na fonte
linha = []

// Define um critério de busca para filtrar os contribuintes pelo tipo de pessoa
filtroMatriculas = "matricula.id = '${p_matricula}'"

// Realiza a busca dos contribuintes aplicando o filtro definido e limitando a 10 registros
dadosMatriculas = fonteMatriculas.busca(criterio: filtroMatriculas, campos: "id, matricula(id, codigo), pessoa(nome)")

// Percorre os resultados obtidos
percorrer (dadosMatriculas) { itemMatriculas ->  
  # Monta a estrutura de cada linha que será inserida na fonte dinâmica
  linha = [
    id: itemMatriculas.id,
    nome: itemMatriculas.pessoa.nome,
    codigo: itemMatriculas.matricula.codigo
  ]
  
  // Exibe os dados no console para conferência
  imprimir linha
  
  // Insere a linha na fonte dinâmica
  fonte.inserirLinha(linha)
}

// Retorna a fonte contendo os registros filtrados e processados
retornar fonte
```