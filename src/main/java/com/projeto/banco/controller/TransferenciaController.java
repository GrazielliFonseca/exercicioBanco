package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Transferencia;
import com.projeto.banco.service.TransferenciaService;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping("/{idCliente}")
    public Transferencia fazerTransferencia(@PathVariable Long idCliente,
                                           @RequestParam Double valorDaTrasferencia,
                                           @RequestParam String contaOrigem,
                                           @RequestParam String contaDestino) {
        return transferenciaService.fazerTransferencia(idCliente, valorDaTrasferencia, contaOrigem, contaDestino);
    }

    @GetMapping("/{id}")
    public Transferencia verTransferencia(@PathVariable Long id) {
        return transferenciaService.verTransferencia(id);
    }

    @GetMapping
    public List<Transferencia> listar() {
        return transferenciaService.listar();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Transferencia> listarPorCliente(@PathVariable Long idCliente) {
        return transferenciaService.listarPorCliente(idCliente);
    }
}