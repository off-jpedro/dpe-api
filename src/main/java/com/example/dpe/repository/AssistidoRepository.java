
package com.example.dpe.repository;

import com.example.dpe.entity.Assistido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AssistidoRepository extends JpaRepository<Assistido, UUID> {

    boolean existsByCpf(String cpf);

    Optional<Assistido> findByIdAndAtivoTrue(UUID id);

    @Query(
            value = """
                SELECT a FROM Assistido a
                WHERE a.ativo = true

                AND (:nome = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.nomeCompleto)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :nome, '%')) AS String))

                AND (:cpf = '' OR a.cpf = :cpf)

                AND (:email = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.email)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :email, '%')) AS String))

                AND (:nacionalidade = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.nacionalidade)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :nacionalidade, '%')) AS String))

                AND (:cidade = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.endereco.cidade)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :cidade, '%')) AS String))

                AND (:situacaoDeRua IS NULL OR
                    a.situacaoDeRua = :situacaoDeRua)
                """,

            countQuery = """
                SELECT COUNT(a) FROM Assistido a
                WHERE a.ativo = true

                AND (:nome = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.nomeCompleto)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :nome, '%')) AS String))

                AND (:cpf = '' OR a.cpf = :cpf)

                AND (:email = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.email)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :email, '%')) AS String))

                AND (:nacionalidade = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.nacionalidade)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :nacionalidade, '%')) AS String))

                AND (:cidade = '' OR
                    CAST(FUNCTION('unaccent', LOWER(a.endereco.cidade)) AS String)
                    LIKE CAST(FUNCTION('unaccent', CONCAT('%', :cidade, '%')) AS String))

                AND (:situacaoDeRua IS NULL OR
                    a.situacaoDeRua = :situacaoDeRua)
                """
    )
    Page<Assistido> buscarAssistidos(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("email") String email,
            @Param("nacionalidade") String nacionalidade,
            @Param("cidade") String cidade,
            @Param("situacaoDeRua") Boolean situacaoDeRua,
            Pageable pageable
    );

}