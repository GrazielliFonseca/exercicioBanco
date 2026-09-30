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
@Table(name = "tb_cartoes")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Cartao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Relacionamento com o Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente idCliente;
    
    @Column(nullable = false)
    private String titular;
    
    @Column(nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private String tipoCartao;
    
    @Column(nullable = false)
    private String bandeira;
    
    @Column(nullable = false)
    private Double rendaInformada;
    
    @Column(nullable = false)
    private String enderecoDeEntrega;
    
    @Column(nullable = false)
    private Double limite;
    
    @Column(nullable = false, unique = true)
    private String numeroCartao;
    
    @Column(nullable = false)
    private String dataDeValidade;
    
    @Column(nullable = false)
    private String cvv;
    
    @Column(nullable = false)
    private String agencia;
    
    @Column(nullable = false)
    private String conta;
    
    @Column(nullable = false, unique = true)
    private String cod;
    
    @Column(nullable = false)
    private String dataSolicitacao;
    
    @Column(nullable = false)
    private String statusPedido;
    
    private String codigoRastreio;
}