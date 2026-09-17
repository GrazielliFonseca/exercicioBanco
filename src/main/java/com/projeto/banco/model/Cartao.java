package com.projeto.banco.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity (name = "tb_cartoes")
@Getter
@Setter
@NoArgsConstructor
public class Cartao {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titular;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String tipoCartao;

    @Column(nullable = false)
    private String bandeira;

    @Column(nullable = false)
    private BigDecimal rendaInformada;

    @Column(nullable = false)
    private String enderecoDeEntrega;

    @Column(nullable = false)
    private BigDecimal limite;

    @Column(nullable = false, unique = true)
    private String numeroCartao;

    @Column(nullable = false)
    private LocalDate dataDeValidade;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private String agencia;

    @Column(nullable = false)
    private String conta;

    @Column(nullable = false)
    private String cod;

    @Column(nullable = false)
    private LocalDate dataSolicitacao;

    @Column(nullable = false)
    private String statusPedido;

    @Column (nullable = true)
    private String codigoRastreio;
}
