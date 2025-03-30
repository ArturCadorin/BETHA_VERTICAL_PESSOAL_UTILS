// Parâmetros
p_Matricula = parametros.pMatricula.selecionado.valor // LISTA SIMPLES DINÂMICA
p_Token = parametros.pToken.valor // CARACTER
p_IdCampoAdicional = parametros.pIdCampoAdicional.valor // CARACTER
p_codigo = parametros.pCodigo.valor // CARACTER

// Método PUT
def Object doPut(Object dadosAtualizar) {
  requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula")
  .cabecalho("Authorization", "Bearer ${p_Token}");
  
  resposta = requisicao.PUT(JSON.escrever(dadosAtualizar), Http.JSON);
  Object respostaObj = JSON.ler(resposta.conteudo());
  return respostaObj;
}

dadosPut = [] 
todosHistoricos = [] 

Dados.pessoal.v2.matriculas.busca(campos: "id, codigoMatricula(numero, contrato)", criterio: "id = $p_Matricula").each { mat -> 
  filtroHistoricos = "codigoMatricula.numero = '$mat.codigoMatricula.numero'"
  Dados.pessoal.v2.historicoMatricula.buscaHistoricoMatricula(criterio: filtroHistoricos).each{ historico -> 
    todosHistoricos << historico
  }
}

todosHistoricos.each{ historico -> 
  requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/$historico.id")
            .cabecalho("Authorization", "Bearer ${p_Token}").GET()
  resposta = requisicao.json();

  resposta.camposAdicionais.each { campo -> 
    if(campo.identificador != 'betha_esfinge_padrao'){
      return
    }
    campo.campos.each { campoItem ->
      if(campoItem.id == p_IdCampoAdicional.toString()){
        def codTCE = campoItem.valor
        def novoCodTCE = p_codigo
        imprimir "COD.TCE ANTES: $codTCE -|- COD.TCE DEPOIS: $novoCodTCE"
        campoItem.valor = novoCodTCE
        imprimir campoItem
      }
    }
  }
  
  dadosPut << [
    "idGerado": resposta['id'],
    "conteudo": resposta
  ]
}


// Realizando requisiçao PUT de 50 em 50 itens
dadosPut.collate(50).each{ hist -> 
	imprimir doPut(hist)
}
