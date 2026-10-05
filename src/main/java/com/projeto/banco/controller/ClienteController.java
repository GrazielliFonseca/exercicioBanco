package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.PessoaFisica;
import com.projeto.banco.model.PessoaJuridica;
import com.projeto.banco.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/pf")
    public Cliente criarPessoaFisica(@RequestBody PessoaFisica pessoaFisica) {
        return clienteService.criarConta(pessoaFisica);
    }

    @PostMapping("/pj")
    public Cliente criarPessoaJuridica(@RequestBody PessoaJuridica pessoaJuridica) {
        return clienteService.criarConta(pessoaJuridica);
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