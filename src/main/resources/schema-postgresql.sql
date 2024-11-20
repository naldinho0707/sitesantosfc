CREATE TABLE IF NOT EXISTS titulos (
    id serial PRIMARY KEY,
    competicao VARCHAR(255),
    qt_titulos VARCHAR(3),
    temporadas VARCHAR(255),
    imagem VARCHAR(255)
);