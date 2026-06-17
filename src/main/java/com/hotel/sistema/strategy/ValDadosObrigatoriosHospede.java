package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;

public class ValDadosObrigatoriosHospede implements IStrategy {
    @Override
    public String processar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        
        if (h.getNomeCompleto() == null || h.getNomeCompleto().isEmpty()) {
            return "Nome completo é obrigatório.";
        }
        
        if (h.getCpf() == null || h.getCpf().isEmpty()) {
            return "CPF é obrigatório.";
        }
        
        if (h.getDataNascimento() == null) {
            return "Data de nascimento é obrigatória.";
        }
        
        if (h.getTelefone() == null || h.getTelefone().isEmpty()) {
            return "Telefone é obrigatório.";
        }
        
        if (h.getEmail() == null || h.getEmail().isEmpty()) {
            return "E-mail é obrigatório.";
        }
        
        return null;
    }
}