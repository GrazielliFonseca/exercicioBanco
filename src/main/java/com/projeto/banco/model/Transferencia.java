package com.projeto.banco.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import lombok.NoArgsConstructor;

@Entity(name = "tb_transferencias")
@Getter
@Setter
@NoArgsConstructor
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "tb_clientes")
     private Long idCliente;
    
     @Column (nullable = false)
     private String valorTransferecencia;
    
     @Column (nullable = false)
     private String contaOrigem;
    
     @OneToMany(mappedBy = "tb_ccontas")
     private Long idConta;
}
