package com.projeto.banco.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cofre;
import com.projeto.banco.repository.CofreRepository;

@Service
public class CofreService {

    private final CofreRepository cofreRepository;

    public CofreService(CofreRepository cofreRepository) {
        this.cofreRepository = cofreRepository;
    }

   public Cofre criarCofre(Long idCliente) {
        if (cofreRepository.findByIdCliente(idCliente).isPresent()) {
            throw erro("Já existe um cofre cadastrado para este usuário.");
        }

        Cofre cofre = new Cofre();
        cofre.setIdCliente(idCliente);
        cofre.setSaldo(0.0);
        cofre.setValor(0.0);

        return cofreRepository.save(cofre);
    }

    public Cofre buscarPorId(Long id) {
        return cofreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cofre com ID " + id + " não encontrado."));
    }

    public Double mostrarValorNoCofre(Long id) {
        Cofre cofre = buscarPorId(id);
        return cofre.getSaldo();
    }

    // + add_valor()
    public Cofre addValor(Long id, Double valorAdicionar) {
        if (valorAdicionar == null || valorAdicionar <= 0) {
            throw erro("O valor a ser adicionado deve ser maior que zero.");
        }

        Cofre cofre = buscarPorId(id);
        cofre.setValor(valorAdicionar);
        cofre.setSaldo(cofre.getSaldo() + valorAdicionar);

        return cofreRepository.save(cofre);
    }

    public Cofre escolherTempoDePermanencia(Long id, Integer meses) {
        if (meses == null || meses <= 0) {
            throw erro("O tempo de permanência deve ser de pelo menos 1 mês.");
        }

        Cofre cofre = buscarPorId(id);
        cofre.setTempoPermanenciaMeses(meses);

        return cofreRepository.save(cofre);
    }

    public Cofre retirarValor(Long id, Double valorRetirar) {
        if (valorRetirar == null || valorRetirar <= 0) {
            throw erro("O valor a ser retirado deve ser maior que zero.");
        }

        Cofre cofre = buscarPorId(id);

        if (cofre.getSaldo() < valorRetirar) {
            throw erro("Saldo insuficiente no cofre para realizar a retirada.");
        }

        cofre.setSaldo(cofre.getSaldo() - valorRetirar);
        cofre.setValor(valorRetirar);

        return cofreRepository.save(cofre);
    }

    // ----- Regras de Validação / Erros -----

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}