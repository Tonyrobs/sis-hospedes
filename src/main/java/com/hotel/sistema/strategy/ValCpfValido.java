package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;

public class ValCpfValido implements IStrategy {

    @Override
    public String processar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        
        if (h.getCpf() == null || h.getCpf().isEmpty()) {
            return null;
        }
        
        String cpf = h.getCpf().replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return "CPF deve conter 11 dígitos.";
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return "CPF inválido.";
        }
        
        // Validação usando algoritmo do dígito verificador
        int sum = 0;
        int remainder;
        
        for (int i = 1; i <= 9; i++) {
            sum += Integer.parseInt(String.valueOf(cpf.charAt(i - 1))) * (11 - i);
        }
        
        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) {
            remainder = 0;
        }
        
        if (remainder != Integer.parseInt(String.valueOf(cpf.charAt(9)))) {
            return "CPF inválido.";
        }
        
        sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += Integer.parseInt(String.valueOf(cpf.charAt(i - 1))) * (12 - i);
        }
        
        remainder = (sum * 10) % 11;
        if (remainder == 10 || remainder == 11) {
            remainder = 0;
        }
        
        if (remainder != Integer.parseInt(String.valueOf(cpf.charAt(10)))) {
            return "CPF inválido.";
        }
        
        return null;
    }
}
