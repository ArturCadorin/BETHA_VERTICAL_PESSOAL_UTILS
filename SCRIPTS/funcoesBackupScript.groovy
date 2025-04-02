p_selecao = parametros.selecaoAvancada?.selecionados?.valor

filtroSelecaoAvancada = ""
if (p_selecao) {
  filtroSelecaoAvancada = "id in (${p_selecao.join(',')})"
}

fonteMatriculas = Dados.pessoal.v2.matriculas;
arquivoBackup = Arquivo.novo("Backup api_folha.txt", 'txt', [encoding: 'iso-8859-1']);

fonteMatriculas.busca(parametros: [selecaoAvancada: filtroSelecaoAvancada]).each{ itemMatriculas -> 
  
  item = itemMatriculas
  
  requisicao = Http.servico("https://pessoal.betha.cloud/service-layer/v1/api/historico-matricula/$item.id")
            .cabecalho("Authorization", "Bearer ${p_Token}").GET()
  resposta = requisicao.json();

  arquivoBackup.escrever("Matricula: $itemMatricula.codigoMatricula.numero - $itemMatriculas.pessoa.nome") // Somente para facilitar identificação do JSON
  arquivoBackup.novaLinha()
  arquivoBackup.escrever(resposta) // JSON backup
  arquivoBackup.novaLinha()
  arquivoBackup.escrever("-----------------------------------------")
  arquivoBackup.novaLinha()
}

Resultado.arquivo(arquivoBackup)
