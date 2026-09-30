package com.projeto.banco.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cartao;
import com.projeto.banco.model.Cliente;
import com.projeto.banco.repository.CartaoRepository;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final ClienteService clienteService;

    public CartaoService(CartaoRepository cartaoRepository, ClienteService clienteService) {
        this.cartaoRepository = cartaoRepository;
        this.clienteService = clienteService;
    }

    public Cartao solicitarCartao(Long idClienteParam, Cartao cartao) {
        if (cartao.getRendaInformada() == null || cartao.getRendaInformada() <= 0) {
            throw erro("A renda informada deve ser maior que zero.");
        }

        Cliente cliente = clienteService.buscarPorId(idClienteParam);

        cartao.setIdCliente(cliente);
        cartao.setDataSolicitacao(LocalDate.now().toString());
        cartao.setStatusPedido("EM_ANALISE");
        
        // Gera dados automáticos para simulação da solicitação
        cartao.setCod(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        cartao.setNumeroCartao("**** **** **** " + (int)(Math.random() * 9000 + 1000));
        cartao.setCvv(String.valueOf((int)(Math.random() * 900 + 100)));
        cartao.setDataDeValidade(LocalDate.now().plusYears(5).toString());

        return cartaoRepository.save(cartao);
    }

    public Cartao buscarPorId(Long id) {
        return cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cartão com ID " + id + " não encontrado."));
    }

    public List<Cartao> listar() {
        return cartaoRepository.findAll();
    }

    public List<Cartao> listarPorCliente(Long idCliente) {
        return cartaoRepository.findByidClient_Id(idCliente);
    }

    // ----- Regra de Erro -----
    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}