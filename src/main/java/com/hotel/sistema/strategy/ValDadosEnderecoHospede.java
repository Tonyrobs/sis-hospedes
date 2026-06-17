package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;

public class ValDadosEnderecoHospede implements IStrategy {
    @Override
    public String processar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        
        if (h.getEndereco() == null) {
            return "Endereço é obrigatório.";
        }
        
        if (h.getEndereco().getLogradouro() == null || h.getEndereco().getLogradouro().isEmpty()) {
            return "Logradouro é obrigatório.";
        }
        
        if (h.getEndereco().getNumero() == null || h.getEndereco().getNumero().isEmpty()) {
            return "Número é obrigatório.";
        }
        
        if (h.getEndereco().getCep() == null || h.getEndereco().getCep().isEmpty()) {
            return "CEP é obrigatório.";
        }
        
        if (h.getEndereco().getBairro() == null || h.getEndereco().getBairro().isEmpty()) {
            return "Bairro é obrigatório.";
        }
        
        if (h.getEndereco().getCidade() == null || h.getEndereco().getCidade().isEmpty()) {
            return "Cidade é obrigatória.";
        }
        
        if (h.getEndereco().getEstado() == null || h.getEndereco().getEstado().isEmpty()) {
            return "Estado é obrigatório.";
        }
        
        return null;
    }
}
