package com.hotel.sistema.controller;

import com.hotel.sistema.dominio.EntidadeDominio;
import com.hotel.sistema.dominio.Hospede;
import com.hotel.sistema.fachada.Fachada;
import com.hotel.sistema.fachada.IFachada;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospedes")
@CrossOrigin(origins = "*")
public class CtrlHospede {

    private final IFachada fachada = new Fachada();

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody Hospede hospede) {
        hospede.setAtivo(true);
        String erro = fachada.salvar(hospede);
        return erro != null ? ResponseEntity.badRequest().body(erro) : ResponseEntity.ok("Cadastrado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> alterar(@PathVariable Long id, @RequestBody Hospede hospede) {
        hospede.setId(id);
        String erro = fachada.alterar(hospede);
        return erro != null ? ResponseEntity.badRequest().body(erro) : ResponseEntity.ok("Atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
        Hospede filtro = new Hospede();
        filtro.setId(id);
        fachada.excluir(filtro);
        return ResponseEntity.ok("Inativado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<EntidadeDominio>> consultar(Hospede filtro) {
        return ResponseEntity.ok(fachada.consultar(filtro));
    }
}