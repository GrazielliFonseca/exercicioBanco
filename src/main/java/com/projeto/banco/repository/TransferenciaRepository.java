package com.projeto.banco.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
}
