package com.projeto.banco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity (name = "tb_contas")
@Getter
@Setter
@NoArgsConstructor
public class Conta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    
     @OneToMany(mappedBy = "tb_clientes")
     private Long idCliente;
    
     @Column(nullable = false)
     private String saldo;
    
     @Column(nullable = false)
     private double valorSaque;
    
     @Column(nullable = false)
     private double valorDeposito;
}
