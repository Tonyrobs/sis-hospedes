package com.hotel.sistema.dao;

import com.hotel.sistema.dominio.EntidadeDominio;
import java.util.List;

public interface IDAO {
    void salvar(EntidadeDominio entidade);
    void alterar(EntidadeDominio entidade);
    void excluir(EntidadeDominio entidade);
    List<EntidadeDominio> consultar(EntidadeDominio entidade);
}