package com.projeto.banco.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente criarConta(Cliente cliente) {
        validarDocumentoUnico(cliente.getDocumento());
        
        if (cliente.getSaldo() == null) {
            cliente.setSaldo(0.0);
        }

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente com ID " + id + " não encontrado."));
    }

    public Double visualizarSaldo(Long id) {
        Cliente cliente = buscarPorId(id);
        return cliente.getSaldo();
    }

    public Cliente depositar(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw erro("O valor do depósito deve ser maior que zero.");
        }

        Cliente cliente = buscarPorId(id);
        cliente.setSaldo(cliente.getSaldo() + valor);

        return clienteRepository.save(cliente);
    }

    public Cliente sacar(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw erro("O valor do saque deve ser maior que zero.");
        }

        Cliente cliente = buscarPorId(id);

        if (cliente.getSaldo() < valor) {
            throw erro("Saldo insuficiente para realizar o saque.");
        }

        cliente.setSaldo(cliente.getSaldo() - valor);

        return clienteRepository.save(cliente);
    }

    private void validarDocumentoUnico(String documento) {
        if (clienteRepository.existsByDocumento(documento)) {
            throw erro("Já existe um cadastro com este documento: " + documento + ".");
        }
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}