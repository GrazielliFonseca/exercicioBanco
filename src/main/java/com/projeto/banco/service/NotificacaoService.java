package com.projeto.banco.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.Notificacao;
import com.projeto.banco.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final ClienteService clienteService;

    public NotificacaoService(NotificacaoRepository notificacaoRepository, ClienteService clienteService) {
        this.notificacaoRepository = notificacaoRepository;
        this.clienteService = clienteService;
    }

    public Notificacao enviarMensagem(Long idClienteParam, String tipoNotificacao, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw erro("A mensagem da notificação não pode estar vazia.");
        }

        if (tipoNotificacao == null || tipoNotificacao.isBlank()) {
            throw erro("O tipo da notificação deve ser informado.");
        }

        Cliente cliente = clienteService.buscarPorId(idClienteParam);

        Notificacao notificacao = new Notificacao();
        notificacao.setIdCliente(cliente);
        notificacao.setTipoNotificacao(tipoNotificacao);
        notificacao.setMensagem(mensagem);

        return notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> listar() {
        return notificacaoRepository.findAll();
    }

    public List<Notificacao> listarPorCliente(Long idCliente) {
        return notificacaoRepository.findByidClient_Id(idCliente);
    }

    public Notificacao buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Notificação com ID " + id + " não encontrada."));
    }

    // ----- Regra de Erro -----
    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}