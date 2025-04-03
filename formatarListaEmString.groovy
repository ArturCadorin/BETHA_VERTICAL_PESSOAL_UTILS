// Exemplo para lista de processamentos:
  processamentos = [
  	procesMensal: 	 evento.processamentoMensal,
    procesFerias: 	 evento.processamentoFerias,
    proces13Salario: evento.processamento13Salario,
    procesRescisao:  evento.processamentoRescisao
  ]
  
  processamento = processamentoCalculo(processamentos)


// Formata a lista
def processamentoCalculo(processamentos) {

    def result = processamentos.findAll { it.value } 
                                   .collect { key, value ->  
                                       formatarCampo(key, value)
                                   }.join(' x ') 
  
    return result 
}

// Função para formatar o campo de acordo com o nome e o valor
def formatarCampo(chave, valor) {
    def mapLabels = [
        'procesMensal': 'MENSAL',
        'procesFerias': 'FERIAS',
        'proces13Salario': '13º SAL',
        'procesRescisao': 'RESCISÃO'
    ]
    
    // Formata o valor do campo como por exemplo: "MENSAL - INTEGRAL"
    return "${mapLabels[chave]} - ${valor[0]}" 
}
