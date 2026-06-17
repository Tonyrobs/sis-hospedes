package com.hotel.sistema.fachada;

import com.hotel.sistema.dao.HospedeDAO;
import com.hotel.sistema.dao.IDAO;
import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import com.hotel.sistema.strategy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fachada implements IFachada {
    private final Map<String, List<IStrategy>> regras = new HashMap<>();
    private final Map<String, IDAO> daos = new HashMap<>();

    public Fachada() {
        HospedeDAO hospedeDAO = new HospedeDAO();
        daos.put(Hospede.class.getName(), hospedeDAO);

        List<IStrategy> regrasHospede = new ArrayList<>();
        regrasHospede.add(new ValDadosObrigatoriosHospede());
        regrasHospede.add(new ValDadosEnderecoHospede());
        regrasHospede.add(new ValCpfValido());
        regrasHospede.add(new ValEmailValido());
        regrasHospede.add(new ValCpfUnico(hospedeDAO));
        regrasHospede.add(new ValEmailUnico(hospedeDAO));
        regrasHospede.add(new CompAuditoriaLog());

        regras.put(Hospede.class.getName(), regrasHospede);
    }

    private String executarRegras(EntidadeDominio entidade) {
        List<IStrategy> strategies = regras.get(entidade.getClass().getName());
        if (strategies != null) {
            for (IStrategy s : strategies) {
                String erro = s.processar(entidade);
                if (erro != null) return erro;
            }
        }
        return null;
    }

    @Override
    public String salvar(EntidadeDominio entidade) {
        String erro = executarRegras(entidade);
        if (erro == null) daos.get(entidade.getClass().getName()).salvar(entidade);
        return erro;
    }

    @Override
    public String alterar(EntidadeDominio entidade) {
        String erro = executarRegras(entidade);
        if (erro == null) daos.get(entidade.getClass().getName()).alterar(entidade);
        return erro;
    }

    @Override
    public String excluir(EntidadeDominio entidade) {
        daos.get(entidade.getClass().getName()).excluir(entidade);
        return null;
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        return daos.get(entidade.getClass().getName()).consultar(entidade);
    }
}