package com.projeto.banco.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.banco.model.Notificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByidClient_Id(Long idCliente);
}