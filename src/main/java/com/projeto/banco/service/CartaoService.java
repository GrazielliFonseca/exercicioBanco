package com.projeto.banco.service;

import org.springframework.stereotype.Service;

import com.projeto.banco.model.Cartao;
import com.projeto.banco.repository.CartaoRepository;

@Service 
public class CartaoService {
    private final CartaoRepository cartaoRepository;
    public CartaoService(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }
    
    public Cartao SolicitarCartao(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }
}
