# DPE-GO — API de Cadastro de Assistidos

Backend desenvolvido em **Java com Spring Boot** para gerenciamento de assistidos, disponibilizando uma API REST responsável pelo cadastro, consulta, atualização, inativação, reativação e exclusão de registros.

A aplicação utiliza arquitetura em camadas, separando as responsabilidades entre Controller, Service e Repository, com persistência de dados no PostgreSQL por meio do Spring Data JPA.

## Funcionalidades

- **Cadastro de assistidos:** criação de registros com informações pessoais, contato, renda e endereço.
- **Consulta por ID:** busca de um assistido pelo seu identificador UUID.
- **Listagem:** consulta de assistidos com paginação e filtros.
- **Listagem de ativos e inativos:** consultas específicas por situação cadastral.
- **Atualização:** edição das informações de um assistido.
- **Inativação:** exclusão lógica, preservando os dados no banco.
- **Reativação:** recuperação de cadastros inativos.
- **Exclusão permanente:** remoção definitiva de registros previamente inativados.
- **Validação de dados:** validação das informações recebidas pela API.
- **Controle de CPF:** verificação de unicidade para evitar cadastros duplicados.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- PostgreSQL
- Maven
- REST API
- UUID
- BigDecimal

## Arquitetura

O projeto utiliza uma arquitetura em camadas, buscando manter o código organizado, desacoplado e de fácil manutenção.

### Responsabilidades

| Camada | Responsabilidade |
|---|---|
| Controller | Receber requisições HTTP e retornar respostas da API. |
| Service | Implementar regras de negócio e coordenar operações. |
| Repository | Realizar operações de persistência e consultas ao banco. |
| Entity | Representar as entidades persistidas no banco de dados. |
| DTO | Definir os dados recebidos e retornados pela API. |
| Mapper | Converter entidades em objetos de resposta. |
| Exception | Centralizar exceções específicas da aplicação. |

## Modelo de dados

O cadastro simplificado de assistidos contempla informações como:

- ID: UUID gerado automaticamente.
- Nome completo.
- CPF.
- Data de nascimento.
- Telefone principal.
- Renda mensal individual.
- E-mail (opcional).
- Nacionalidade.
- Situação de rua.
- Endereço (quando aplicável).
- Situação do cadastro (ativo/inativo).
- Data de cadastro gerada automaticamente.

A renda mensal é representada por `BigDecimal` na aplicação Java e armazenada como `NUMERIC(12,2)` no PostgreSQL.

## Endpoints da API

Endpoint base:

```text
http://localhost:8080/api/assistidos
```

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/assistidos` | Cadastrar assistido |
| GET | `/api/assistidos` | Listar assistidos ativos |
| GET | `/api/assistidos/{id}` | Buscar assistido por ID |
| GET | `/api/assistidos/inativos` | Listar assistidos inativos |
| GET | `/api/assistidos/todos` | Listar todos os assistidos |
| PUT | `/api/assistidos/{id}` | Atualizar assistido |
| DELETE | `/api/assistidos/{id}` | Inativar assistido |
| PATCH | `/api/assistidos/{id}/reativar` | Reativar assistido |
| DELETE | `/api/assistidos/{id}/permanente` | Excluir permanentemente |

### Paginação e filtros

As listagens permitem paginação e ordenação por meio de parâmetros como:

- `page`: número da página.
- `size`: quantidade de registros por página.
- `sort`: campo e direção da ordenação.

Também podem ser utilizados filtros por informações cadastrais, como nome, CPF, e-mail, nacionalidade, cidade e situação de rua.

Exemplo:

```http
GET /api/assistidos?page=0&size=10&sort=nomeCompleto,asc
```

## Regras de negócio importantes

- Cada assistido possui um identificador UUID gerado automaticamente.
- O CPF deve ser único entre os cadastros.
- A data de cadastro é gerada automaticamente pelo sistema.
- A renda mensal utiliza `BigDecimal` para preservar a precisão dos valores monetários.
- A inativação não remove fisicamente o registro do banco de dados.
- Somente assistidos inativos podem ser excluídos permanentemente.
- A consulta individual considera apenas assistidos ativos.
- Quando o assistido está em situação de rua, o endereço não é obrigatório.

## Pré-requisitos

Para executar o projeto localmente, é necessário ter instalado:

- JDK compatível com a versão configurada no projeto.
- Maven.
- PostgreSQL.
- IDE de sua preferência (IntelliJ IDEA, Eclipse ou VS Code).

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/SEU-USUARIO/dpe-go-assistido-api.git
```

### 2. Entrar na pasta do projeto

```bash
cd dpe-go-assistido-api
```

### 3. Configurar o banco de dados

Crie um banco de dados PostgreSQL e configure as propriedades de conexão no arquivo `application.properties` ou `application.yml`, conforme a configuração utilizada no projeto.

Não compartilhe credenciais reais do banco de dados no repositório.

### 4. Instalar dependências e compilar

```bash
mvn clean install
```

### 5. Executar a aplicação

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Integração com o Frontend

Este backend fornece os serviços REST consumidos pelo frontend desenvolvido em Angular.

O frontend realiza as operações de cadastro, consulta, edição e gerenciamento da situação dos assistidos por meio dos endpoints disponibilizados pela API.

## Tratamento de exceções

A aplicação utiliza exceções específicas para representar situações de negócio, como:

- Assistido não encontrado.
- CPF já cadastrado.
- Tentativa de exclusão permanente de registro que não está inativo.

As regras de negócio são mantidas na camada de serviço, evitando concentrar essa responsabilidade nos controllers.

---

**Projeto:** DPE-GO — Cadastro de Assistidos  
**Backend:** Java + Spring Boot  
**Persistência:** Spring Data JPA / Hibernate  
**Banco de dados:** PostgreSQL  
**Arquitetura:** Camadas (Controller, Service e Repository)
