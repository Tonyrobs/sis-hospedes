package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CompAuditoriaLog implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHora = LocalDateTime.now().format(formatador);

        String tipoOperacao = (entidade.getId() == null) ? "NOVO CADASTRO" : "ALTERAÇÃO";
        String detalhes = "";

        if (entidade instanceof Hospede) {
            Hospede h = (Hospede) entidade;
            detalhes = " | Nome: " + h.getNomeCompleto() + " (CPF: " + h.getCpf() + ")";

            if (h.getId() != null) {
                detalhes += " | ID: " + h.getId();
            }
        }

        System.out.println("[AUDITORIA] " + tipoOperacao + " na tabela "
                + entidade.getClass().getSimpleName().toUpperCase()
                + detalhes
                + " | Data: " + dataHora);

        return null;
    }
}