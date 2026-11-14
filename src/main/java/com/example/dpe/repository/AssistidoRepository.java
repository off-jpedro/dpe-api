package com.example.dpe.repository;

import com.example.dpe.entity.Assistido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssistidoRepository extends JpaRepository<Assistido, UUID> {
    boolean existsByCpf(String cpf);
}
