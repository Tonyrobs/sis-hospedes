package com.hotel.sistema.fachada;

import com.hotel.sistema.dominio.EntidadeDominio;
import java.util.List;

public interface IFachada {
    String salvar(EntidadeDominio entidade);
    String alterar(EntidadeDominio entidade);
    String excluir(EntidadeDominio entidade);
    List<EntidadeDominio> consultar(EntidadeDominio entidade);
}