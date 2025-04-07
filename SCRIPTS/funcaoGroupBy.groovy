
filtro = "competencia >= '2025-01' and competencia <= '2025-03'"

dados = Dados.folha.v2.remuneracoes.busca(criterio: filtro)

competencias = dados.groupBy { it.competencia }

competencias.each { competencia, itens ->
  imprimir "Competência: ${competencia}"
  itens.each { item ->
    imprimir "Mat: $item.matricula.codigoMatricula.numero | Sequencial: $item.sequencial | Código eSocial: $item.identificadorFolhaDePagamento"
  }
  imprimir ""
}
