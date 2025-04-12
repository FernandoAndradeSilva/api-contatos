# HubSpot Contact Integration - Spring Boot Application

## Visão Geral

Esta aplicação Java, desenvolvida com **Spring Boot 3** e **Java 21**, integra-se à API do HubSpot utilizando o fluxo de autenticação **OAuth 2.0 (Authorization Code Flow)**. A funcionalidade principal é autenticar o usuário e permitir a **criação de contatos** em sua conta HubSpot.

## Arquitetura

A arquitetura adotada é baseada no padrão **Onion Architecture**, promovendo desacoplamento, testabilidade e separação clara de responsabilidades:

- **Domain:** Contém as entidades e contratos (interfaces) do domínio.
- **Application (Controller):** Fornece os endpoints REST da aplicação.
- **Infrastructure:** Contém implementações técnicas como chamadas HTTP, serviços e utilitários.

Essa estrutura é compatível com os princípios do **Domain-Driven Design (DDD)** e aplica o **Princípio da Inversão de Dependência (DIP)** do SOLID.

## Endpoints

| Método | Endpoint                        | Descrição                                         |
|--------|----------------------------------|--------------------------------------------------|
| GET    | `/oauth/authorize`              | Gera a URL de autorização do HubSpot             |
| GET    | `/oauth/callback?code=...`      | Processa o código de autorização                |
| POST   | `/hubspot/contacts`             | Cria um novo contato na conta HubSpot autenticada |

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Maven
- OAuth 2.0
- SLF4J / LoggerFactory
- RestTemplate

## Como Executar

```bash
git clone https://github.com/seu-usuario/nome-do-repo.git
```

Acesse:
- Autorização OAuth: http://localhost:8080/oauth/authorize


## Autor

Desenvolvido por Fernando  
[LinkedIn](https://www.linkedin.com/in/fernando-andrade-b14a26112/) • [GitHub](https://github.com/FernandoAndradeSilva)

