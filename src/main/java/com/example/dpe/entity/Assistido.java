
package com.example.dpe.entity;

import com.example.dpe.entity.Endereco;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assistidos")
public class Assistido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_completo", nullable = false, length = 200)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone_principal", nullable = false, length = 20)
    private String telefonePrincipal;

    @Column(name = "renda_mensal", nullable = false, precision = 12, scale = 2)
    private BigDecimal rendaMensal;

    @Column(length = 254)
    private String email;

    @Column(length = 100)
    private String nacionalidade;

    @Column(name = "situacao_de_rua")
    private Boolean situacaoDeRua;

    @Embedded
    private Endereco endereco;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Assistido() {
    }

    public Assistido(
            String nomeCompleto,
            String cpf,
            LocalDate dataNascimento,
            String telefonePrincipal,
            BigDecimal rendaMensal
    ) {
        this(nomeCompleto, cpf, dataNascimento, telefonePrincipal,
                rendaMensal, null, null, null, null);
    }

    public Assistido(
            String nomeCompleto,
            String cpf,
            LocalDate dataNascimento,
            String telefonePrincipal,
            BigDecimal rendaMensal,
            String email,
            String nacionalidade,
            Boolean situacaoDeRua,
            Endereco endereco
    ) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefonePrincipal = telefonePrincipal;
        this.rendaMensal = rendaMensal;
        this.email = email;
        this.nacionalidade = nacionalidade;
        this.situacaoDeRua = situacaoDeRua;
        this.endereco = endereco;
        this.ativo = true;
    }

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public String getEmail() {
        return email;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public Boolean getSituacaoDeRua() {
        return situacaoDeRua;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setSituacaoDeRua(Boolean situacaoDeRua) {
        this.situacaoDeRua = situacaoDeRua;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void desativar() {
        this.ativo = false;
    }
}
