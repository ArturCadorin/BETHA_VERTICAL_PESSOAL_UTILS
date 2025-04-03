// Padrão do arquivo -> 1234|ARTUR DE JESUS CADORIN

p_arquivo = parametros.arquivo.valor;
p_token = parametros.token.valor;

fonteConfiguracaoEvento = Dados.folha.v2.configuracaoEvento;

arquivo = Arquivo.ler(p_arquivo, 'txt');

eventos = []
while(arquivo.contemProximaLinha()) {
  linha = arquivo.lerLinha().toString()
  valores = linha.split(/\|/)
  codigoEvento = Long.valueOf(valores[0])
  descricaoEvento = valores[1]
  
  dadosConfiguracaoEvento = fonteConfiguracaoEvento.busca(criterio: "codigo = $codigoEvento").each { evento -> 
    
	idEvento = evento.id
    
    body = [
      "idGerado": idEvento,
      "conteudo": [
      	"id": idEvento
      ]
    ]
    eventos << body 
  }
}
