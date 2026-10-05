package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public Cliente criarConta(@RequestBody Cliente cliente) {
        return clienteService.criarConta(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/{id}/saldo")
    public Double visualizarSaldo(@PathVariable Long id) {
        return clienteService.visualizarSaldo(id);
    }

    @PutMapping("/{id}/depositar")
    public Cliente depositar(@PathVariable Long id, @RequestParam Double valor) {
        return clienteService.depositar(id, valor);
    }

    @PutMapping("/{id}/sacar")
    public Cliente sacar(@PathVariable Long id, @RequestParam Double valor) {
        return clienteService.sacar(id, valor);
    }
}