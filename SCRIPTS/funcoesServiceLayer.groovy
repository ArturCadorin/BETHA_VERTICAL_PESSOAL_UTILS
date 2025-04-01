// Token por parâmetro de entrada 
p_Token = parametros.pToken.valor // CARACTER ou SENHA

// Token chumbado no código
//p_Token = "3d23d-32s3-3d3ds-3d3d3d-3s33xd"

// Exemplo de método GET utilizando a CHAVE_INTEGRACAO
requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/$itemCargo.id")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).GET()

// Exemplo de método GET utilizando o Token de Conversão
requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/$itemCargo.id")
            .cabecalho("Authorization", "Bearer ${p_Token}").GET()


// Convertendo a requisição em um JSON
resposta = requisicao.json();

// Acessando e manipulando os campos de um JSON 
resposta.cargo.id = p_cargoNovo.toLong() 
resposta.vagas.cargo.cargo[0].id = p_cargoNovo.toLong() 

// Montando o body
body = [
    "idIntegracao": resposta['id'],
    "conteudo": resposta
  ]
  
// Convertendo o body em um JSON válido
body = JSON.escrever([body])
  
// Exemplo de método POST utilizando a CHAVE_INTEGRACAO
envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/")
  		.chaveIntegracao(Variavel.CHAVE_INTEGRACAO).POST(body, Http.JSON)

// Exemplo de método POST utilizando o Token de Conversão
envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/$itemCargo.id")
            .cabecalho("Authorization", "Bearer ${p_Token}").POST(body, Http.JSON)

// Convertendo o POST em um JSON novamente
envia = envia.json()
// Conferindo o lote enviado através do ID do body recém manipulado
confereLote(envia.id)

// Função para conferir o lote enviado
def confereLote(idLote){
  
  loteExecutado = false
  while(!loteExecutado){
    lote = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/lotes/${idLote}")
    	.chaveIntegracao(Variavel.CHAVE_INTEGRACAO).GET()
    lote = lote.json()
    
    if(lote.situacao == "EXECUTADO"){
      loteExecutado = true
      
      imprimir "---------------RESULTADO DO LOTE----------------"
      imprimir lote
      
      break
    }
    esperar 2.segundos
  }
  return
}