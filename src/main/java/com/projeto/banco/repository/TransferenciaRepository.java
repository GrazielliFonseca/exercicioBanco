package com.projeto.banco.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByidClient_Id(Long idCliente);
}