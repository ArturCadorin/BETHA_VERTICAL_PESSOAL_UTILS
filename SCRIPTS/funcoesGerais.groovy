///////////////////// Critérios de folha /////////////////////
filtroFolha = "competencia = '${p_competencia}' and tipoProcessamento = '${p_processamento}' and subTipoProcessamento = '${p_subtipoProcessamento}'"
if (p_matricula) {
  filtroFolha += " and matricula.id = (${p_matricula.join(',')})"
}

filtroSelecaoAvancada = ""
if (p_selecao) {
  filtroSelecaoAvancada = "id in (${p_selecao.join(',')})"
}

///////////////////// Revisar e alterar (lista simples) /////////////////////
p_opcao = parametros.opcaoExecucao?.selecionado?.valor // Lista simples (REVISAR, ALTERAR)
if(p_opcao && p_opcao == "REVISAR"){
  /*
   processamento para revisão de dados apenas, não realizar alteração aqui.
  */
}else if(p_opcao && p_opcao == "ALTERAR"){
  // Exemplo de alteração de dados
  listaFolhas.collate(50).each { folhaCollate -> 
   body = JSON.escrever(folhaCollate)
    
   envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/folha/")
  		.cabecalho("Authorization", "Bearer ${p_token}").POST(body, Http.JSON)
   envia = envia.json()
   arquivoLote.escrever(confereLote(envia.id))
   arquivoLote.novaLinha()
  }
  Resultado.arquivo(arquivoRevisao)
  Resultado.arquivo(arquivoBackup)
  Resultado.arquivo(arquivoLote)
}

