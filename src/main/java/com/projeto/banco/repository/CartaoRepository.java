package com.projeto.banco.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.banco.model.Cartao;
import com.projeto.banco.model.Conta;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByConta(Conta conta);
}
