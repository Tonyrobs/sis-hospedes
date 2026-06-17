package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import java.util.regex.Pattern;

public class ValEmailValido implements IStrategy {
    private static final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public String processar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        if (h.getEmail() != null && !Pattern.compile(REGEX).matcher(h.getEmail()).matches()) {
            return "O formato do e-mail inserido é inválido.";
        }
        return null;
    }
}