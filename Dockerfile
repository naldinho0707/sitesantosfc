# FROM openjdk:17-jdk-alpine  A imagem oficial openjdk foi descontinuada, usar Eclipse Temurin ou Amazon Corretto.

FROM eclipse-temurin:17-jdk-alpine

# vai montar uma máquina (pequena) virtual só para rodar o java
# porque o render não tem suporte nativo para java

# criar uma pasta dentro da máquina virtual
WORKDIR /app

# comando copy vai o arquivo pom.xml e joga para pasta app
#  que esta representada pelo ponto  "."  pasta raiz

# copiar a descrição do projeto, dependencia
COPY pom.xml .
# executavel do maven
COPY mvnw .
# maven
COPY .mvn .mvn
# copiar o font o código
COPY src ./src

# da a permisão para porra toda  pode usar o +x  777 é para tudo
RUN chmod 777 mvnw

# quero gerar o war é o maven, executar:
#RUN ./mvnw package

#ignorar testes (RUN ./mvnw package) para carregar as variaveis no render, porque os testes não vão passar sem as variaveis de ambiente
RUN ./mvnw package -DskipTests

# mostra o nome do arquivo
# RUN ls -l ./target

# Cria a pasta de uploads manualmente para garantir que ela exista
RUN mkdir -p target/classes/static/uploads && chmod 777 target/classes/static/uploads

# cmd executar o java (arquivo war) na pasta target do projeto sitedb: target/sitesantosfc.war
CMD ["java","-jar","target/sitesantosfc-0.0.1-SNAPSHOT.war"]






