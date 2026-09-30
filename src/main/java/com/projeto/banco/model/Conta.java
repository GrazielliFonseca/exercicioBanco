package com.projeto.banco.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "tb_contas")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Conta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "Cliente_id", nullable = false)
    private Cliente idCliente;
    
    @Column(nullable = false)
    private Double saldo = 0.0;
    
    @Column(nullable = true)
    private Double valorDoSaque = 0.0;
    
    @Column(nullable = true)
    private Double valorDoDeposito = 0.0;
}