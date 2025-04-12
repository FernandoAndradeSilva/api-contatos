# 📬 HubSpot Integration API

Esta é uma aplicação Java com integração à API do HubSpot via OAuth 2.0, que permite autenticar, obter um token de acesso e criar contatos no CRM da HubSpot.

---

## 🧠 Sumário

- [Objetivo](#objetivo)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura Onion](#arquitetura-onion)
- [Instalação e Execução](#instalação-e-execução)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Autenticação OAuth 2.0](#autenticação-oauth-20)
- [Criação de Contatos](#criação-de-contatos)
- [Boas Práticas de Segurança Aplicadas](#boas-práticas-de-segurança-aplicadas)
- [Tratamento de Erros e Logs](#tratamento-de-erros-e-logs)
- [Considerações Finais](#considerações-finais)

---

## 🎯 Objetivo

Este projeto tem como finalidade integrar uma aplicação Java com o HubSpot CRM para:

- Gerar URL de autorização OAuth 2.0
- Processar o callback da autorização e armazenar tokens
- Criar contatos no HubSpot
- Tratar limites de requisições (rate limiting)
- Aplicar práticas de segurança e arquitetura limpa

---

## 🛠 Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Web
- Maven
- SLF4J + Logback
- Jackson
- HubSpot API (OAuth + CRM)

---

## 🧱 Arquitetura Onion

A arquitetura Onion (Cebola) foi escolhida por isolar bem as responsabilidades e permitir que a lógica de negócio não dependa diretamente de detalhes técnicos.

### Camadas da Aplicação:

- **Application (Controller):** Recebe requisições HTTP e orquestra a lógica.
- **Domain:** Contém os contratos e modelos de negócio.
- **Infrastructure:** Implementa os serviços e utilitários, separados da lógica de domínio.

### Benefícios:

- Baixo acoplamento entre camadas
- Alta coesão e reusabilidade
- Facilita testes e evolução do sistema
- Segue o DIP (Princípio da Inversão de Dependência - SOLID)
- Alinhada com conceitos de DDD (Domain-Driven Design)

---

## ⚙️ Instalação e Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/hubspot-integration.git
    ````
2. Configure o application.properties:

Adicione as seguintes configurações no seu arquivo `application.properties` ou `application.yml` para garantir a integração com o HubSpot:

### `application.properties`

```properties
# HubSpot OAuth2 - Credenciais e configurações
hubspot.client-id=SEU_CLIENT_ID
hubspot.client-secret=SEU_CLIENT_SECRET
hubspot.redirect-uri=http://localhost:8080/oauth/callback
hubspot.scope=crm.objects.contacts.write crm.objects.contacts.read
hubspot.auth-url=https://app.hubspot.com/oauth/authorize
````



# 📡 Endpoints Disponíveis

| Método | URL                     | Descrição                          |
|--------|--------------------------|-------------------------------------|
| GET    | `/oauth/authorize`       | Gera URL de autorização OAuth       |
| GET    | `/oauth/callback`        | Recebe o código da HubSpot          |
| POST   | `/hubspot/contacts`      | Cria um novo contato no HubSpot     |

---

## 🔐 Autenticação OAuth 2.0

- Acesse `/oauth/authorize` para redirecionar ao HubSpot.
- Após consentimento, o HubSpot redireciona para `/oauth/callback?code=xxx`.
- A aplicação troca o código por um `access_token`, armazenado temporariamente em memória (`TokenStore`).

---

## 🧾 Criação de Contatos

**Requisição:**

```http
POST /hubspot/contacts
Content-Type: application/json

{
  "email": "joao@email.com",
  "firstname": "João",
  "lastname": "Silva"
}
````
# 🧾 Respostas da API

**Respostas:**

- ✅ **201 Created**: Contato criado com sucesso
- ❌ **401 Unauthorized**: Token ausente ou expirado
- ⚠️ **400 Bad Request**: Campos obrigatórios ausentes

---

# 🛡️ Boas Práticas de Segurança Aplicadas

Asseguramos conformidade com as recomendações da HubSpot:

- 🔐 **Tokens de acesso não são persistidos**: Armazenados apenas em memória.
- 🛑 **Dados sensíveis ocultos**: `client_secret`, tokens e dados pessoais não são expostos em logs ou mensagens de erro.
- 🔁 **Retry com controle de Rate Limit**: Requisições 429 são reexecutadas com backoff (delay) de forma automática.
- 🔎 **Escopos mínimos necessários**: Apenas `crm.objects.contacts.read` e `write` foram utilizados.
- ⚠️ **Validação e sanitização de entrada**: Dados do usuário validados antes de processar.

---

# 🪵 Tratamento de Erros e Logs

- **SLF4J + Logback**: Usado para rastreamento completo da aplicação.
- **Logs informativos**: Incluímos tentativa, URL e respostas HTTP.
- **Sem exposição de informações sensíveis**: Nenhum token ou dado do cliente aparece nos logs.
- **Exceções personalizadas**: Mensagens tratadas de forma amigável e segura para o usuário final.


# Dependências do Projeto

Este projeto utiliza o Maven como ferramenta de gerenciamento de dependências. Abaixo estão listadas as principais dependências configuradas no `pom.xml`:

## Dependências Principais

1. **Spring Boot Starter OAuth2 Client**
    - **Group ID**: `org.springframework.boot`
    - **Artifact ID**: `spring-boot-starter-oauth2-client`
    - **Descrição**: Fornece funcionalidades para integrar autenticação OAuth2 com clientes, essencial para comunicação com a API do HubSpot.

2. **Spring Boot Starter Web**
    - **Group ID**: `org.springframework.boot`
    - **Artifact ID**: `spring-boot-starter-web`
    - **Descrição**: Adiciona as dependências necessárias para criar uma aplicação web com o Spring Boot. Inclui o Tomcat, Jackson, e outras bibliotecas necessárias para trabalhar com HTTP e APIs RESTful.

5. **Lombok**
    - **Group ID**: `org.projectlombok`
    - **Artifact ID**: `lombok`
    - **Descrição**: Biblioteca que reduz o código boilerplate, como getters, setters, `toString`, `equals`, e `hashCode`. Usada principalmente com anotações.
    - **Escopo**: `annotationProcessor`

## Configuração do Java

- **Versão do Java**: 21

## Plugin de Build

- **Spring Boot Maven Plugin**: Usado para empacotar e executar a aplicação Spring Boot através do Maven.

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
````



# 📌 Considerações Finais

A aplicação está preparada para **escalar**, **testável** e com **estrutura profissional**. A combinação da **arquitetura Onion** com **boas práticas de segurança** resulta em um sistema **robusto**, **manutenível** e alinhado aos padrões modernos de integração com **APIs externas** como a **HubSpot**.

