package com.projeto.banco.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.banco.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmail(String email);
    
}
