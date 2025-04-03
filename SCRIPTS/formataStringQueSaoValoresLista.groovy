  processamentos = [
  	procesMensal: 	 evento.processamentoMensal,
    procesFerias: 	 evento.processamentoFerias,
    proces13Salario: evento.processamento13Salario,
    procesRescisao:  evento.processamentoRescisao
  ]

imprimir processamentos // {procesMensal=[INTEGRAL], procesFerias=[INTEGRAL], proces13Salario=[INTEGRAL], procesRescisao=[]}

processamento = processamentoCalculo(processamentos) // MENSAL - INTEGRAL, FERIAS - INTEGRAL, 13º SAL - INTEGRAL

// Formatar processamentos
def processamentoCalculo(processamentos) {
    // Filtra os campos que têm valores preenchidos e formata a string
    def result = processamentos.findAll { it.value } // Filtra somente os campos não vazios
                                   .collect { key, value -> 
                                       // Formata os campos conforme o nome e valor
                                       formatarCampo(key, value)
                                   }.join(', ') // Junta os resultados com uma vírgula
 
    println result // Imprime a string resultante
}

// Função para formatar o campo de acordo com o nome e o valor
def formatarCampo(chave, valor) {
    def mapLabels = [
        'procesMensal': 'MENSAL',
        'procesFerias': 'FERIAS',
        'proces13Salario': '13º SAL',
        'procesRescisao': 'RESCISÃO'
    ]
    
    // Formata o valor do campo como "Nome - INTEGRAL"
    return "${mapLabels[chave]} - ${valor[0]}" // Aqui estamos assumindo que o valor é uma lista e pegando o primeiro item
}
