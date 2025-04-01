# 📌 Documentação do script [ALTERAÇÃO] Insere evento na folha de pagamento SEM compor base
---
Este script insere um evento em uma folha de pagamento já calculada/fechada.
⚠️ Para que a alteração seja realizada com sucesso a competência do eSocial deve estar aberta!

﻿# 📑 Guia preenchimento dos parâmetros
---
![Parâmetros](../../../SCRIPTS/screenshots/Screenshot_1.png)
### TOKEN DE CONVERSÃO (service-layer):
- Inserir o token para realização do service layer.
### COMPETÊNCIA CALCULADA:
- Competência na qual a folha está calculada.
### PROCESSAMENTO:
- Tipo de processamento da folha (Mensal, Férias, Rescisão e 13º Salário).
### SUBTIPO PROCESSAMENTO:
- Subtipo de processamento da folha (Adiantamento, Integral e Complementar).
### É UMA RÉPLICA?
- O evento a ser inserido é uma réplica? Caso SIM irá inserir o evento como uma réplica.
### MATRÍCULA:
- Matrícula que irá ter a folha ajustada.
### SELEÇÃO AVANÇADA:
- Seleção avançada de matrículas que terão suas folhas ajustada.
### EVENTO PARA INSERIR:
- Evento que será inserido na folha.
### VALOR CALCULADO:
- Valor calculado do evento a ser inserido na folha.
### VALOR REFERENCIA:
- Valor referência do evento a ser inserido na folha.
### EXECUÇÃO E VALIDAÇÃO:
- Revisar e Validar o JSON:
  - Retorna um arquivo TXT para validação da folha a ser ajustada.
  - Validação dos dados como: Total Líquido, Total Bruto e Total Desconto.
- Validar e Executar o POST:
  - Retorna três arquivos TXT, sendo: Arquivo de validação, arquivo de backup e arquivo com a impressão do lote do SL.
  - Realiza o POST de 50 em 50 folhas. 
