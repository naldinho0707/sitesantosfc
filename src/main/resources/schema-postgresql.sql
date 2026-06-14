/*SQL script para criar as tabelas no PostgreSQL*/

CREATE TABLE IF NOT EXISTS titulos (
    id SERIAL PRIMARY KEY,
    competicao VARCHAR(255),
    qt_titulos VARCHAR(3),
    temporadas VARCHAR(255),
    imagem VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS perfil_usuario (
    id SERIAL PRIMARY KEY,
    usuarioid UUID NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    CONSTRAINT fk_perfil_usuario FOREIGN KEY (usuarioid) REFERENCES usuario(id),
    CONSTRAINT perfil_usuario_unique UNIQUE (usuarioid)
);

CREATE TABLE IF NOT EXISTS jogador (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    matricula VARCHAR(5) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    idade INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS campeonato (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    temporada VARCHAR(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS inscricao (
    jogador_id UUID REFERENCES jogador(id) ON DELETE CASCADE,
    campeonato_id UUID REFERENCES campeonato(id) ON DELETE CASCADE,
    data_inscricao DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (jogador_id, campeonato_id)
);