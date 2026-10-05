package com.projeto.banco.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projeto.banco.model.Notificacao;
import com.projeto.banco.service.NotificacaoService;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @PostMapping("/{idCliente}")
    public Notificacao enviarMensagem(@PathVariable Long idCliente,
                                     @RequestParam String tipoNotificacao,
                                     @RequestParam String mensagem) {
        return notificacaoService.enviarMensagem(idCliente, tipoNotificacao, mensagem);
    }

    @GetMapping
    public List<Notificacao> listar() {
        return notificacaoService.listar();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Notificacao> listarPorCliente(@PathVariable Long idCliente) {
        return notificacaoService.listarPorCliente(idCliente);
    }

    @GetMapping("/{id}")
    public Notificacao buscarPorId(@PathVariable Long id) {
        return notificacaoService.buscarPorId(id);
    }
}