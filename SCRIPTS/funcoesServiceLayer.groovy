// Token por parâmetro de entrada 
p_Token = parametros.pToken.valor // CARACTER 

//////////////////////////////// Chamada de métodos http ////////////////////////////////
// Existem duas formas de executar requisições http, por chave de integração e por token de conversão.
// Chave de integração dos scripts (para entidades que necessitam de uso continuo do script) -> .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).GET()
// Token de conversão (para alterações pontuais ou uso em serviços de tratamento) -> .cabecalho("Authorization", "Bearer ${p_Token}").GET()
// Para métodos que alteram dados, deve-se passar um body tratado que será o json.

// ==== Requisições GET
//def item = item que está percorrendo a fonte dinâmica
requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/$item.id")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).GET()

requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/$item.id")
            .cabecalho("Authorization", "Bearer ${p_Token}").GET()
resposta = requisicaoGET.json(); // Converte a requisição GET em um JSON manipulável através do script

// ==== Requisições POST
envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).POST(body, Http.JSON)

envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .cabecalho("Authorization", "Bearer ${p_token}").POST(body, Http.JSON)

// ==== Requisições DELETE
deleta = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).DELETE(body, Http.JSON)

deleta = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .cabecalho("Authorization", "Bearer ${p_token}").DELETE(body, Http.JSON)

// ==== Requisições PUT
atualiza = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).PUT(body, Http.JSON)

atualiza = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/")
            .cabecalho("Authorization", "Bearer ${p_token}").PUT(body, Http.JSON)

//////////////////////////////// Tratando o JSON oriundo do método GET ////////////////////////////////
// Exemplos de tratamento através da resposta obtida na requisição GET -> resposta = requisicaoGET.json();
resposta.cargo.id = p_cargoNovo.toLong() // Troca o cargo da matrícula
resposta.situacao = "DEMITIDO" // Troca a situação da matrícula

//////////////////////////////// Montando e Preparando o JSON para método Http ////////////////////////////////
// Body para requisições GET, POST e PUT
// 1º exemplo - Esta forma de montar o body utiliza da resposta obtida na requisição GET -> resposta = requisicaoGET.json();
body = [
    "idIntegracao": resposta['id'],
    "conteudo": resposta
]

// Body para requisições GET, POST e PUT
// 2º exemplo - Esta forma de montar o body utiliza da fonte dinâmica, sem o uso da requisição GET
body = [
    "idIntegracao": item.id,
    "conteudo": [
       "id": item.id,
       "situacao": p_situacao,
       "cargo": [
           "id": p_cargoNovo.toLong()        
       ]
    ]
  ]

// Body para requisições DELETE
// Muito mais fácil realizar diretamente pela fonte dinâmica, sem o uso da requisição GET
body = [
    "idGerado": item.id,
    "conteudo": [
       "id": item.id
    ]
]

// Convertendo o body em um JSON válido para passar nas requisições http
body = JSON.escrever([body])
  
//////////////////////////////// Função para conferir lotes ////////////////////////////////
// 1º exemplo - Retorna a impressão do lotes no log de execução
def confereLote(idLote){
  
  loteExecutado = false
  while(!loteExecutado){
    lote = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/lotes/${idLote}")
    	.cabecalho("Authorization", "Bearer ${p_Token}").GET()
    lote = lote.json()
    
    if(lote.situacao == "EXECUTADO"){
      loteExecutado = true
      
      imprimir "---------------RESULTADO DO LOTE----------------"
      imprimir lote
      
      break
    }
    esperar 2.segundos // Alterar caso for necessário (impacta diretamente no tempo de execução do script)
  }
  return
}
// Formatando requisição realizada e chamando a função confereLote
envia = envia.json()
confereLote(envia.id)

// 2º exemplo - Retorna a impressão do lotes em um arquivo TXT
def confereLote(idLote){
  loteExecutado = false
  impressaoLote = ""
  while(!loteExecutado){
    lote = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/folha/lotes/${idLote}")
    	.cabecalho("Authorization", "Bearer ${p_token}").GET()
    lote = lote.json()
    
    if(lote.situacao == "EXECUTADO"){
      loteExecutado = true
      impressaoLote = lote
      
      break
    }
    esperar 2.segundos // Alterar caso seja necessário
  }
  return impressaoLote
}
// Escrevendo o arquivo TXT com o retorno do lotes
arquivoLote.escrever(confereLote(envia.id))
arquivoLote.novaLinha()

//////////////////////////////// Alterando os dados com requisições http ////////////////////////////////
// 1º exemplo -> Realiza as requisições de uma em uma.
body = JSON.escrever([body])
envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/folha/")
  	.cabecalho("Authorization", "Bearer ${p_token}").POST(body, Http.JSON)
envia = envia.json()
confereLote(envia.id)

// 2º exemplo -> Realiza as requisições de 50 em 50 (recomendado para alterações em massa)
listaFolhas.collate(50).each { folhaCollate -> 
   body = JSON.escrever(folhaCollate)
    
   envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/folha/")
  		.cabecalho("Authorization", "Bearer ${p_token}").POST(body, Http.JSON)
   envia = envia.json()
   arquivoLote.escrever(confereLote(envia.id))
   arquivoLote.novaLinha()
}
