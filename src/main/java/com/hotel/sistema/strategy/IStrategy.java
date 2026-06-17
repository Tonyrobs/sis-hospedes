package com.hotel.sistema.strategy;

import com.hotel.sistema.dominio.EntidadeDominio;

public interface IStrategy {
    String processar(EntidadeDominio entidade);
}