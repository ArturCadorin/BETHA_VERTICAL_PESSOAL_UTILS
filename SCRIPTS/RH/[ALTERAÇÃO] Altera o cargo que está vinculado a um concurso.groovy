p_idConcurso = parametros.p_idConcurso.valor; // CARACTER
p_cargoAntigo = parametros.p_cargoAntigo.selecionado.valor; // LISTA SIMPLES DINÂMICA
p_cargoNovo = parametros.p_cargoNovo.selecionado.valor; // LISTA SIMPLES DINÂMICA

// Inserir esse filtro avançado nos parâmetros dos cargos:
// Criterio.onde('origem.id').ehNulo()

// Caso a chave de integração não funcionar:
// .cabecalho("Authorization", "Bearer ${p_Token}")

body = [] 
Dados.rh.v1.concursoCargo.busca(criterio: "concurso.id = ${p_idConcurso}", campos: "id, concurso(descricao), cargo(id, descricao), vagas(cargo(cargo(id, descricao)))").each { itemCargo -> 
 
  idCargoAntigo = itemCargo.cargo.id
  if(p_cargoAntigo != idCargoAntigo.toString()){
    suspender "ID do cargo não foi encontrado no concurso: '${itemCargo.concurso.descricao}'"
  }
  
  requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/$itemCargo.id")
            .chaveIntegracao(Variavel.CHAVE_INTEGRACAO).GET()
  resposta = requisicao.json();
  resposta.cargo.id = p_cargoNovo.toLong() // concurso-processo-seletivo-cargo --> { cargo: "id" }
  resposta.vagas.cargo.cargo[0].id = p_cargoNovo.toLong() // concurso-precesso-seletivo-cargo --> { vagas: { cargo: { cargo: "id" }}}
  imprimir "$resposta"
  body = [
    "idIntegracao": resposta['id'],
    "conteudo": resposta
  ]
  
  body = JSON.escrever([body])
  
  envia = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/concurso-processo-seletivo-cargo/")
  		.chaveIntegracao(Variavel.CHAVE_INTEGRACAO).POST(body, Http.JSON)
  envia = envia.json()
  confereLote(envia.id)

}

// Confere o lote enviado
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