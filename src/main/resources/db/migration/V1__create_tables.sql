CREATE TABLE tabela_tarifaria (
                                  id BIGSERIAL PRIMARY KEY,
                                  nome VARCHAR(150) NOT NULL,
                                  data_vigencia DATE NOT NULL,
                                  ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE categoria_tarifaria (
                                     id BIGSERIAL PRIMARY KEY,
                                     categoria VARCHAR(50) NOT NULL,
                                     tabela_tarifaria_id BIGINT NOT NULL,
                                     CONSTRAINT fk_categoria_tarifaria_tabela
                                         FOREIGN KEY (tabela_tarifaria_id)
                                             REFERENCES tabela_tarifaria(id)
                                             ON DELETE CASCADE
);

CREATE TABLE faixa_consumo (
                               id BIGSERIAL PRIMARY KEY,
                               inicio INTEGER NOT NULL,
                               fim INTEGER NOT NULL,
                               valor_unitario NUMERIC(10,2) NOT NULL,
                               categoria_tarifaria_id BIGINT NOT NULL,
                               CONSTRAINT fk_faixa_consumo_categoria
                                   FOREIGN KEY (categoria_tarifaria_id)
                                       REFERENCES categoria_tarifaria(id)
                                       ON DELETE CASCADE,
                               CONSTRAINT chk_faixa_inicio_menor_fim
                                   CHECK (inicio < fim),
                               CONSTRAINT chk_valor_unitario_nao_negativo
                                   CHECK (valor_unitario >= 0)
);
