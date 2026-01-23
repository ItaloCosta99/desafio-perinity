# Desafio Perinity - Sistema GRC (Gestão de Relacionamento de Clientes)

Este projeto é uma API REST moderna desenvolvida em **Java 17** com **Quarkus**, focada no gerenciamento de clientes, produtos e vendas. A aplicação segue princípios de arquitetura limpa e entrega **100% de cobertura de testes**.

## 🚀 Funcionalidades

- **Gerenciamento de Clientes:** CRUD completo e relatórios de novos clientes por ano.
- **Gerenciamento de Produtos:** CRUD completo e relatório de produtos mais antigos.
- **Gerenciamento de Vendas:** Registro de vendas com cálculo automático de impostos (9%) e totais, além de relatórios de faturamento mensal e produtos mais vendidos.
- **Documentação:** Swagger UI integrado para exploração da API.

## 🛠 Tecnologias e Ferramentas

- **Java 17** (Linguagem base)
- **Quarkus 3.30.6** (Framework principal)
- **MongoDB** (Banco de dados NoSQL)
- **Panache MongoDB** (Simplificação da persistência)
- **JUnit 5 & Mockito** (Testes unitários e mocks)
- **REST Assured** (Testes de integração de API)
- **JaCoCo** (Análise de cobertura de código)
- **GitHub Actions** (CI/CD com MongoDB integrado)

## 🏗 Arquitetura

A aplicação foi estruturada seguindo princípios de **Hexagonal Architecture** (Portas e Adaptadores) e **DDD (Domain Driven Design)**, dividida em:

- **Application Domain:** Modelos de domínio puro e exceções.
- **Application Service:** Lógica de negócio e orquestração.
- **Infrastructure Inbound:** Pontos de entrada REST e DTOs.
- **Infrastructure Outbound:** Implementações de persistência (Adaptadores).

## 🧪 Testes e Qualidade

O projeto mantém rigorosos padrões de qualidade:

- **Cobertura de Código:** **100%** de linhas e instruções cobertas (validado via plugin JaCoCo).
- **Testes de Integração:** Validam o fluxo completo desde o endpoint até o banco de dados.
- **Check de Cobertura:** O build falha automaticamente se a cobertura cair abaixo de 100%.

Para rodar os testes e gerar o relatório de cobertura:

```bash
mvn clean verify
```

O relatório será gerado em `target/jacoco-report/index.html`.

## 🏃 Como Rodar a Aplicação

### Pré-requisitos

- JDK 17+
- Maven 3.8+
- Instância do MongoDB (local ou Atlas)

### Modo Desenvolvimento

```bash
./mvnw compile quarkus:dev
```

A API estará disponível em `http://localhost:8080`.
O Swagger UI pode ser acessado em `http://localhost:8080/q/swagger-ui`.

### Via Docker

```bash
docker build -t desafio-perinity .
docker run -p 8080:8080 -e MONGODB_PASSWORD=sua_senha desafio-perinity
```

## 📈 CI/CD

O projeto utiliza **GitHub Actions** para:

- Execução automatizada de testes a cada push/pull request.
- Validação de 100% de cobertura de código.
- Build da imagem Docker.
- Armazenamento de artefatos de cobertura.

---

Desenvolvido como parte do desafio técnico da Perinity.
