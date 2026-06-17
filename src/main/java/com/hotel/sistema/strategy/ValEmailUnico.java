package com.hotel.sistema.strategy;

import com.hotel.sistema.dao.HospedeDAO;
import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import java.util.List;

public class ValEmailUnico implements IStrategy {

    private final HospedeDAO dao;

    public ValEmailUnico(HospedeDAO dao) {
        this.dao = dao;
    }

    @Override
    public String processar(EntidadeDominio entidade) {
        if (entidade instanceof Hospede) {
            Hospede hospede = (Hospede) entidade;

            if (hospede.getEmail() == null || hospede.getEmail().trim().isEmpty()) {
                return null;
            }

            Hospede filtro = new Hospede();
            filtro.setEmail(hospede.getEmail());


            List<EntidadeDominio> resultados = dao.consultar(filtro);

            if (!resultados.isEmpty()) {
                Hospede encontrado = (Hospede) resultados.get(0);
                if (hospede.getId() == null || !hospede.getId().equals(encontrado.getId())) {
                    return "Erro: O e-mail '" + hospede.getEmail() + "' já está em uso por outro hóspede.";
                }
            }
        }
        return null;
    }
}