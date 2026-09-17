package com.projeto.banco.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {}
