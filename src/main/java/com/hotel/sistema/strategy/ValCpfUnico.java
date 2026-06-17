package com.hotel.sistema.strategy;

import com.hotel.sistema.dao.IDAO;
import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import java.util.List;

public class ValCpfUnico implements IStrategy {
    private final IDAO dao;

    public ValCpfUnico(IDAO dao) {
        this.dao = dao;
    }

    @Override
    public String processar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        Hospede filtro = new Hospede();
        filtro.setCpf(h.getCpf());

        List<EntidadeDominio> resultado = dao.consultar(filtro);
            if (resultado != null && !resultado.isEmpty()) {
            if (h.getId() != null && resultado.get(0).getId().equals(h.getId())) return null;
            return "O CPF informado já pertence a outro hóspede.";
        }
        return null;
    }
}