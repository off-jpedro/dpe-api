CREATE TABLE assistidos (
                            id UUID PRIMARY KEY,
                            nome_completo VARCHAR(200) NOT NULL,
                            cpf VARCHAR(11) NOT NULL UNIQUE,
                            data_nascimento DATE NOT NULL,
                            telefone_principal VARCHAR(20) NOT NULL,
                            renda_mensal NUMERIC(12, 2) NOT NULL
                                CHECK (renda_mensal >= 0),
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);