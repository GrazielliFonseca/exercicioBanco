package com.projeto.banco.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.Conta;
import com.projeto.banco.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteService clienteService;

    public ContaService(ContaRepository contaRepository, ClienteService clienteService) {
        this.contaRepository = contaRepository;
        this.clienteService = clienteService;
    }

    public Conta criarConta(Long idClienteParam, Conta conta) {
        if (contaRepository.findByidClientId(idClienteParam).isPresent()) {
            throw erro("Já existe uma conta cadastrada para este cliente.");
        }
        
        Cliente cliente = clienteService.buscarPorId(idClienteParam);
        
        conta.setIdCliente(cliente);
        
        if (conta.getSaldo() == null) {
            conta.setSaldo(0.0);
        }

        return contaRepository.save(conta);
    }

    public List<Conta> listar() {
        return contaRepository.findAll();
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta com ID " + id + " não encontrada."));
    }

    public Double visualizarSaldo(Long id) {
        Conta conta = buscarPorId(id);
        return conta.getSaldo();
    }

    public Conta fazerDeposito(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw erro("O valor do depósito deve ser maior que zero.");
        }

        Conta conta = buscarPorId(id);
        conta.setValorDoDeposito(valor);
        conta.setSaldo(conta.getSaldo() + valor);

        return contaRepository.save(conta);
    }

    public Conta fazerSaque(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw erro("O valor do saque deve ser maior que zero.");
        }

        Conta conta = buscarPorId(id);

        if (conta.getSaldo() < valor) {
            throw erro("Saldo insuficiente para realizar o saque.");
        }

        conta.setValorDoSaque(valor);
        conta.setSaldo(conta.getSaldo() - valor);

        return contaRepository.save(conta);
    }
    
    // ----- Regra de Erro -----
    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
