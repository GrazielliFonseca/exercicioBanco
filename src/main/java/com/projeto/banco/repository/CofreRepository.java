package com.projeto.banco.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Cofre;

public interface CofreRepository extends JpaRepository<Cofre, Long> {

    Optional<Cofre> findByIdCliente(Long idCliente);
}