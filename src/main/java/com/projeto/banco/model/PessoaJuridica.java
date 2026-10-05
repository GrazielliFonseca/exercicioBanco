package com.projeto.banco.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PJ")
@NoArgsConstructor
public class PessoaJuridica extends Cliente {

    public PessoaJuridica(String nome, String documento, String email) {
        super(nome, documento, email);
    }

    public String getCnpj() {
        return getDocumento();
    }

    @Override
    public String getTipo() {
        return "PJ";
    }

    @Override
    public String getNomeDoDocumento() {
        return "CNPJ";
    }
}