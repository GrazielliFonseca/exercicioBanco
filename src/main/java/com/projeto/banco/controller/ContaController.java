package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Conta;
import com.projeto.banco.service.ContaService;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/{idCliente}")
    public Conta criarConta(@PathVariable Long idCliente, @RequestBody Conta conta) {
        return contaService.criarConta(idCliente, conta);
    }

    @GetMapping
    public List<Conta> listar() {
        return contaService.listar();
    }

    @GetMapping("/{id}/saldo")
    public Double visualizarSaldo(@PathVariable Long id) {
        return contaService.visualizarSaldo(id);
    }

    @PutMapping("/{id}/depositar")
    public Conta fazerDeposito(@PathVariable Long id, @RequestParam Double valor) {
        return contaService.fazerDeposito(id, valor);
    }

    @PutMapping("/{id}/sacar")
    public Conta fazerSaque(@PathVariable Long id, @RequestParam Double valor) {
        return contaService.fazerSaque(id, valor);
    }
}