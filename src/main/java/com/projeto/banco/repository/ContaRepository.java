package com.projeto.banco.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    Optional<Conta> findByidClientId(Long idCliente);
}