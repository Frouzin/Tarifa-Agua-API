# Tarifa Água API

API REST desenvolvida em Java com Spring Boot para gerenciamento de tabelas tarifárias de água e cálculo de tarifas com base no consumo e categoria do consumidor.

## Tecnologias Utilizadas

* Java 17
* Spring Boot 4
* Spring Data JPA
* PostgreSQL 16
* Flyway
* Maven
* Docker
* Swagger/OpenAPI

---

# Instruções de Instalação e Execução

## Clonar o projeto

```bash
git clone https://github.com/Frouzin/Tarifa-Agua-API.git
```

Entrar na pasta:

```bash
cd Tarifa-Agua-API
```

## Gerar Build

```bash
mvn clean package
```

## Executar a aplicação

### Linux/Mac

```bash
./mvnw spring-boot:run
```

### Windows

```cmd
mvnw.cmd spring-boot:run
```

---

# Pré-requisitos

Antes de executar a aplicação, certifique-se de possuir:

| Ferramenta     | Versão |
| -------------- | ------ |
| Java           | 17+    |
| Maven          | 3.9+   |
| PostgreSQL     | 16+    |
| Docker         | 20+    |
| Docker Compose | 2+     |

Verificar versões instaladas:

```bash
java --version
mvn --version
docker --version
docker compose version
```

---

# Configuração do Banco de Dados

O projeto utiliza PostgreSQL como banco de dados relacional.

## Utilizando Docker

Suba o banco de dados:

```bash
docker compose up -d
```

Verifique se o container está em execução:

```bash
docker ps
```

## Configuração da aplicação

Arquivo:

```text
src/main/resources/application.yml
```

Exemplo de configuração:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tarifa_agua
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

Ao iniciar a aplicação, o Flyway executará automaticamente as migrations presentes em:

```text
src/main/resources/db/migration
```

---

# Swagger

Após iniciar a aplicação, a documentação interativa estará disponível em:

```text
http://localhost:8080/swagger-ui.html
```

ou

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Exemplos de Requests e Responses

## Criar Tabela Tarifária

### POST /tabelas-tarifarias

### Request

```json
{
  "nome": "Tabela 2026",
  "dataVigencia": "2026-01-01",
  "categorias": [
    {
      "categoria": "INDUSTRIAL",
      "faixas": [
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 1.00
        },
        {
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 2.00
        },
        {
          "inicio": 21,
          "fim": 99999,
          "valorUnitario": 3.00
        }
      ]
    }
  ]
}
```

### Response

```json
{
  "id": 1,
  "nome": "Tabela 2026",
  "dataVigencia": "2026-01-01",
  "ativa": true
}
```

---

## Listar Tabelas Tarifárias

### GET /tabelas-tarifarias

### Response

```json
[
  {
    "id": 1,
    "nome": "Tabela 2026",
    "dataVigencia": "2026-01-01",
    "ativa": true
  }
]
```

---

## Excluir Tabela Tarifária

### DELETE /tabelas-tarifarias/{id}

### Exemplo

```http
DELETE /tabelas-tarifarias/1
```

### Response

```http
204 No Content
```

---

## Calcular Tarifa

### POST /api/calculos

Calcula o valor da tarifa com base na categoria do consumidor e no consumo informado, retornando também o detalhamento por faixa tarifária utilizada no cálculo.

### Request

```json
{
  "categoria": "INDUSTRIAL",
  "consumo": 25
}
```

### Response

```json
{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 25,
  "valorTotal": 45.00,
  "detalhamento": [
    {
      "faixa": {
        "inicio": 0,
        "fim": 10
      },
      "m3Cobrados": 10,
      "valorUnitario": 1.00,
      "subtotal": 10.00
    },
    {
      "faixa": {
        "inicio": 11,
        "fim": 20
      },
      "m3Cobrados": 10,
      "valorUnitario": 2.00,
      "subtotal": 20.00
    },
    {
      "faixa": {
        "inicio": 21,
        "fim": 99999
      },
      "m3Cobrados": 5,
      "valorUnitario": 3.00,
      "subtotal": 15.00
    }
  ]
}
```

### Campos da Resposta

| Campo         | Descrição                                                  |
| ------------- | ---------------------------------------------------------- |
| categoria     | Categoria do consumidor utilizada no cálculo               |
| consumoTotal  | Consumo total informado em m³                              |
| valorTotal    | Valor final da tarifa calculada                            |
| detalhamento  | Lista contendo o cálculo realizado em cada faixa tarifária |
| faixa.inicio  | Início da faixa de consumo                                 |
| faixa.fim     | Fim da faixa de consumo                                    |
| m3Cobrados    | Quantidade de metros cúbicos cobrados naquela faixa        |
| valorUnitario | Valor cobrado por m³ na faixa                              |
| subtotal      | Valor parcial calculado para a faixa                       |

---

# Tratamento de Erros

## Erro de Validação

### Response

```json
{
  "timestamp": "2026-06-15T06:00:00",
  "status": 400,
  "error": "Erro de validação",
  "mensagens": [
    "nome: O nome da tabela tarifária é obrigatório."
  ]
}
```

## Erro de Regra de Negócio

### Response

```json
{
  "timestamp": "2026-06-15T06:00:00",
  "status": 400,
  "error": "Erro de regra de negócio",
  "mensagens": [
    "Categoria duplicada na tabela tarifária."
  ]
}
```

---

# Como Testar a Aplicação

## Cadastro de Tabela Tarifária

Valide os seguintes cenários:

* Cadastro válido
* Categoria duplicada
* Faixa inicial diferente de 0
* Faixas com lacunas
* Faixas sobrepostas
* Última faixa insuficiente
* Nome obrigatório
* Categoria inválida

## Cálculo de Tarifa

Valide os seguintes cenários:

* Consumo igual a 0
* Consumo dentro da primeira faixa
* Consumo em múltiplas faixas
* Consumo elevado
* Categoria inexistente
* Consumo negativo
* Conferência do detalhamento por faixa
* Validação dos subtotais e do valor total calculado

## Persistência

Valide os seguintes cenários:

* Criação automática das tabelas via Flyway
* Persistência dos dados no PostgreSQL
* Exclusão lógica das tabelas tarifárias

---

# Estrutura do Projeto

```text
src
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── service
└── resources
    ├── application.yml
    └── db/migration
```

---

# Autor

Matheus Reis

Projeto desenvolvido como desafio técnico para avaliação de conhecimentos em Java, Spring Boot, PostgreSQL, Flyway e desenvolvimento de APIs REST.
