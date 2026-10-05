package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Cartao;
import com.projeto.banco.service.CartaoService;

@RestController
@RequestMapping("/api/cartoes")
public class CartaoController {

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping("/{idCliente}")
    public Cartao solicitarCartao(@PathVariable Long idCliente, @RequestBody Cartao cartao) {
        return cartaoService.solicitarCartao(idCliente, cartao);
    }

    @GetMapping
    public List<Cartao> listar() {
        return cartaoService.listar();
    }

    @GetMapping("/{id}")
    public Cartao buscarPorId(@PathVariable Long id) {
        return cartaoService.buscarPorId(id);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Cartao> listarPorCliente(@PathVariable Long idCliente) {
        return cartaoService.listarPorCliente(idCliente);
    }
}
