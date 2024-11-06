# Pegando a imagem do Maven direto do DockerHub
FROM maven:3.9.9 AS build

# Criando o diretório de trabalho no container
WORKDIR /app

# Copiando o arquivo .xml e os arquivos Java para o diretorio de trabalho
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

# Expondo a porta 8080
EXPOSE 8080

# Executando a aplicação
CMD ["mvn", "spring-boot:run"]
