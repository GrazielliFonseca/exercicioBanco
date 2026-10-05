package com.projeto.banco;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cartao;
import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.PessoaFisica;
import com.projeto.banco.repository.CartaoRepository;
import com.projeto.banco.service.CartaoService;
import com.projeto.banco.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CartaoService cartaoService;

    @Test
    @DisplayName("deve solicitar cartão com sucesso quando a renda for válida")
    void solicitarCartaoSucesso() {
        Cliente cliente = new PessoaFisica("Mariana", "99988877766", "mariana@email.com");
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(cartaoRepository.save(any(Cartao.class))).thenAnswer(inv -> inv.getArgument(0));

        Cartao novoCartao = new Cartao();
        novoCartao.setRendaInformada(3000.0);
        novoCartao.setTipoCartao("CREDITO");

        Cartao cartaoSolicitado = cartaoService.solicitarCartao(1L, novoCartao);

        assertNotNull(cartaoSolicitado);
        assertEquals("EM_ANALISE", cartaoSolicitado.getStatusPedido());
        assertNotNull(cartaoSolicitado.getCod());
        assertNotNull(cartaoSolicitado.getCvv());
    }

    @Test
    @DisplayName("não deve solicitar cartão com renda zero ou negativa")
    void solicitarCartaoRendaInvalida() {
        Cartao novoCartao = new Cartao();
        novoCartao.setRendaInformada(0.0);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            cartaoService.solicitarCartao(1L, novoCartao);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(cartaoRepository, never()).save(any(Cartao.class));
    }
}