package com.projeto.banco.controller;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Cofre;
import com.projeto.banco.service.CofreService;

@RestController
@RequestMapping("/api/cofres")
public class CofreController {

    private final CofreService cofreService;

    public CofreController(CofreService cofreService) {
        this.cofreService = cofreService;
    }

    @PostMapping("/{idCliente}")
    public Cofre criarCofre(@PathVariable Long idCliente) {
        return cofreService.criarCofre(idCliente);
    }

    @GetMapping("/{id}/saldo")
    public Double mostrarValorNoCofre(@PathVariable Long id) {
        return cofreService.mostrarValorNoCofre(id);
    }

    @PutMapping("/{id}/adicionar")
    public Cofre addValor(@PathVariable Long id, @RequestParam Double valor) {
        return cofreService.addValor(id, valor);
    }

    @PutMapping("/{id}/tempo-permanencia")
    public Cofre escolherTempoDePermanencia(@PathVariable Long id, @RequestParam Integer meses) {
        return cofreService.escolherTempoDePermanencia(id, meses);
    }

    @PutMapping("/{id}/retirar")
    public Cofre retirarValor(@PathVariable Long id, @RequestParam Double valor) {
        return cofreService.retirarValor(id, valor);
    }
}