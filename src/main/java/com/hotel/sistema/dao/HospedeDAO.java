package com.hotel.sistema.dao;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Endereco;
import com.hotel.sistema.dominio.Hospede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAO implements IDAO {

    private Connection getConexao() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:hoteldb", "sa", "");
    }

    public HospedeDAO() {
        String sql = "CREATE TABLE IF NOT EXISTS hospedes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nome_completo VARCHAR(255), " +
                "cpf VARCHAR(20), " +
                "data_nascimento DATE, " +
                "telefone VARCHAR(20), " +
                "email VARCHAR(255), " +
                "logradouro VARCHAR(255), " +
                "numero VARCHAR(20), " +
                "cep VARCHAR(20), " +
                "bairro VARCHAR(100), " +
                "complemento VARCHAR(255), " +
                "cidade VARCHAR(100), " +
                "estado VARCHAR(5), " +
                "ativo BOOLEAN)";

        try (Connection conn = getConexao(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        Endereco end = h.getEndereco() != null ? h.getEndereco() : new Endereco();

        String sql = "INSERT INTO hospedes (nome_completo, cpf, data_nascimento, telefone, email, logradouro, numero, cep, bairro, complemento, cidade, estado, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConexao(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, h.getNomeCompleto());
            stmt.setString(2, h.getCpf());
            stmt.setDate(3, h.getDataNascimento() != null ? Date.valueOf(h.getDataNascimento()) : null);
            stmt.setString(4, h.getTelefone());
            stmt.setString(5, h.getEmail());

            stmt.setString(6, end.getLogradouro());
            stmt.setString(7, end.getNumero());
            stmt.setString(8, end.getCep());
            stmt.setString(9, end.getBairro());
            stmt.setString(10, end.getComplemento());
            stmt.setString(11, end.getCidade());
            stmt.setString(12, end.getEstado());

            stmt.setBoolean(13, h.isAtivo());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    h.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        Endereco end = h.getEndereco() != null ? h.getEndereco() : new Endereco();

        String sql = "UPDATE hospedes SET nome_completo=?, cpf=?, data_nascimento=?, telefone=?, email=?, logradouro=?, numero=?, cep=?, bairro=?, complemento=?, cidade=?, estado=?, ativo=? WHERE id=?";

        try (Connection conn = getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.getNomeCompleto());
            stmt.setString(2, h.getCpf());
            stmt.setDate(3, h.getDataNascimento() != null ? Date.valueOf(h.getDataNascimento()) : null);
            stmt.setString(4, h.getTelefone());
            stmt.setString(5, h.getEmail());

            stmt.setString(6, end.getLogradouro());
            stmt.setString(7, end.getNumero());
            stmt.setString(8, end.getCep());
            stmt.setString(9, end.getBairro());
            stmt.setString(10, end.getComplemento());
            stmt.setString(11, end.getCidade());
            stmt.setString(12, end.getEstado());

            stmt.setBoolean(13, h.isAtivo());
            stmt.setLong(14, h.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(EntidadeDominio entidade) {
        Hospede h = (Hospede) entidade;
        String sql = "UPDATE hospedes SET ativo = false WHERE id = ?";

        try (Connection conn = getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, h.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        Hospede filtro = (Hospede) entidade;
        List<EntidadeDominio> resultados = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM hospedes WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();

        if (filtro.getCpf() != null && !filtro.getCpf().isEmpty()) {
            sql.append(" AND cpf = ?");
            params.add(filtro.getCpf());
        } else if (filtro.getEmail() != null && !filtro.getEmail().isEmpty()) {
            sql.append(" AND email = ?");
            params.add(filtro.getEmail());
        } else if (filtro.getId() != null) {
            sql.append(" AND id = ?");
            params.add(filtro.getId());
        } else if (filtro.getNomeCompleto() != null && !filtro.getNomeCompleto().isEmpty()) {
            sql.append(" AND nome_completo LIKE ?");
            params.add("%" + filtro.getNomeCompleto() + "%");
        } else {
            sql.append(" AND ativo = true");
        }

        try (Connection conn = getConexao(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hospede h = new Hospede();
                    h.setId(rs.getLong("id"));
                    h.setNomeCompleto(rs.getString("nome_completo"));
                    h.setCpf(rs.getString("cpf"));
                    Date data = rs.getDate("data_nascimento");
                    if (data != null) {
                        h.setDataNascimento(data.toLocalDate());
                    }
                    h.setTelefone(rs.getString("telefone"));
                    h.setEmail(rs.getString("email"));
                    h.setAtivo(rs.getBoolean("ativo"));

                    Endereco end = new Endereco();
                    end.setLogradouro(rs.getString("logradouro"));
                    end.setNumero(rs.getString("numero"));
                    end.setCep(rs.getString("cep"));
                    end.setBairro(rs.getString("bairro"));
                    end.setComplemento(rs.getString("complemento"));
                    end.setCidade(rs.getString("cidade"));
                    end.setEstado(rs.getString("estado"));

                    h.setEndereco(end);
                    resultados.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }
}