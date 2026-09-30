package com.projeto.banco.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity (name = "tb_cofres")
@Getter
@Setter
@NoArgsConstructor
public class Cofre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    
    @Column(nullable = false)
    private Long idCliente;

    @Column(nullable = false)
    private Double saldo = 0.0;
    
    @Column(nullable = false)
    private Double valor = 0.0;

    private Integer tempoPermanenciaMeses; // Auxiliar para a função de tempo de permanência

}
