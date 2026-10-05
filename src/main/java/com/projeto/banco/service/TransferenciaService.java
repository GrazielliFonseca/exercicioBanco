package com.projeto.banco.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.Transferencia;
import com.projeto.banco.repository.TransferenciaRepository;

@Service
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ClienteService clienteService;

    public TransferenciaService(TransferenciaRepository transferenciaRepository, ClienteService clienteService) {
        this.transferenciaRepository = transferenciaRepository;
        this.clienteService = clienteService;
    }

    public Transferencia fazerTransferencia(Long idClienteParam, Double valorDaTrasferencia, String contaOrigem, String contaDestino) {
        if (valorDaTrasferencia == null || valorDaTrasferencia <= 0) {
            throw erro("O valor da transferência deve ser maior que zero.");
        }

        if (contaOrigem == null || contaOrigem.isBlank() || contaDestino == null || contaDestino.isBlank()) {
            throw erro("A conta de origem e a conta de destino devem ser informadas.");
        }

        if (contaOrigem.equals(contaDestino)) {
            throw erro("A conta de origem não pode ser igual à conta de destino.");
        }

        Cliente cliente = clienteService.buscarPorId(idClienteParam);

        Transferencia transferencia = new Transferencia();
        transferencia.setIdCliente(cliente);
        transferencia.setValorDaTrasferencia(valorDaTrasferencia);
        transferencia.setContaOrigem(contaOrigem);
        transferencia.setContaDestino(contaDestino);

        return transferenciaRepository.save(transferencia);
    }

    public Transferencia verTransferencia(Long id) {
        return transferenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transferência com ID " + id + " não encontrada."));
    }

    public List<Transferencia> listar() {
        return transferenciaRepository.findAll();
    }

    public List<Transferencia> listarPorCliente(Long idCliente) {
        return transferenciaRepository.findByidClient_Id(idCliente);
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}