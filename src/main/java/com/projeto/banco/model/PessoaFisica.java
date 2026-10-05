package com.projeto.banco.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PF")
@NoArgsConstructor
public class PessoaFisica extends Cliente {

    public PessoaFisica(String nome, String documento, String email) {
        super(nome, documento, email);
    }

    public String getCpf() {
        return getDocumento();
    }

    @Override
    public String getTipo() {
        return "PF";
    }

    @Override
    public String getNomeDoDocumento() {
        return "CPF";
    }
}