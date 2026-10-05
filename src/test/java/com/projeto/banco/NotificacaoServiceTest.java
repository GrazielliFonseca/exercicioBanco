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

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.Notificacao;
import com.projeto.banco.model.PessoaFisica;
import com.projeto.banco.repository.NotificacaoRepository;
import com.projeto.banco.service.ClienteService;
import com.projeto.banco.service.NotificacaoService;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Test
    @DisplayName("deve enviar e salvar notificação com sucesso")
    void enviarMensagemSucesso() {
        Cliente cliente = new PessoaFisica("Fernanda", "11122233344", "fernanda@email.com");
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(notificacaoRepository.save(any(Notificacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Notificacao notificacao = notificacaoService.enviarMensagem(1L, "SMS", "Sua chave PIX foi cadastrada.");

        assertNotNull(notificacao);
        assertEquals("SMS", notificacao.getTipoNotificacao());
        assertEquals("Sua chave PIX foi cadastrada.", notificacao.getMensagem());
        assertEquals(cliente, notificacao.getIdCliente());
    }

    @Test
    @DisplayName("não deve enviar notificação com mensagem vazia")
    void enviarMensagemVazia() {
        Cliente cliente = new PessoaFisica("Fernanda", "11122233344", "fernanda@email.com");
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            notificacaoService.enviarMensagem(1L, "PUSH", "   ");
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }
}