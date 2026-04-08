# Laborwaze Queue System

Sistema de gerenciamento de filas para atendimento healthcare.

## Objetivo

Gerenciar o fluxo de atendimento em unidades de saúde, permitindo:

- Registro de pacientes na fila com senha automática
- Diferenciação por prioridade (Normal, Prioridade, Urgente)
- Chamada de pacientes para atendimento por atendentes em salas/setores
- Notificações em tempo real via WebSocket para painéis de chamada
- Geração de relatórios em PDF com filtro por período
- Controle de acesso por JWT (usuários podem ser PACIENTE, ATENDENTE, ADMIN ou SUPERVISOR)

## Arquitetura

Arquitetura hexagonal (ports and adapters) com camadas bem definidas:

```
src/main/java/com/laborwaze/queue_system/
├── domain/          # Camada central: entidades, enums, interfaces de repositório
│   ├── model/       # Entidades JPA (Usuario, Paciente, Chamada, Sala, Setor, Servico)
│   ├── enums/       # Enums de domínio (PapelUsuario, StatusChamada, NivelPrioridade)
│   └── repository/  # Interfaces Spring Data JPA (ports inbound)
│
├── application/     # Regras de negócio: services, DTOs, ports
│   ├── service/     # Services com lógica de negócio
│   ├── dto/         # Request/Event DTOs (ChamadaRequest, PainelEventoDTO)
│   └── port/        # Interfaces de ports outbound (PdfGeneratorPort, PainelPublisherPort)
│
├── api/             # Camada de apresentação: controllers, requests, responses
│   ├── controller/  # Controllers REST
│   ├── request/     # Request DTOs específicos de controllers
│   └── response/    # Response DTOs
│
├── infra/           # Adaptações outbound: WebSocket, report generation
│   ├── websocket/   # Implementação STOMP para painéis
│   └── report/      # Geração de PDF com JasperReports
│
└── config/          # Configuração: security, websocket, OpenAPI, exception handling
```

### Fluxo de dependência

```
api → application → domain ← infra
config → (todas as camadas)
```

A camada `domain` não tem dependências externas. Ports são definidos na `domain`/`application`, implementações ficam na `infra`.

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem |
| Spring Boot | 3.5.x | Framework |
| Spring Security | - | Autenticação JWT stateless |
| Spring Data JPA | - | Persistência |
| Spring WebSocket | - | Notificações em tempo real (STOMP) |
| PostgreSQL | - | Banco de dados |
| Flyway | - | Migrações de banco |
| H2 | - | Banco de testes |
| Lombok | - | Boilerplate reduction |
| JasperReports | - | Geração de relatórios PDF |
| SpringDoc OpenAPI | 2.8.9 | Swagger UI |
| JJWT | 0.12.6 | Token JWT |

## Como rodar

### Pré-requisitos

- Java 21
- Maven 3.9+ (ou `mvnw` incluso no projeto)
- PostgreSQL rodando

### 1. Configurar banco de dados

```bash
# Criar banco
createdb laborwaze

# As migrações são aplicadas automaticamente pelo Flyway ao iniciar
```

### 2. Configurar variáveis de ambiente (opcional)

```bash
export DB_URL=jdbc:postgresql://localhost:5432/laborwaze
export DB_USERNAME=postgres
export DB_PASSWORD=dev2026
export JWT_SECRET=my-secret-key-at-least-32-chars-long
export SERVER_PORT=8080
```

Os valores padrão estão em `src/main/resources/application.yml`.

### 3. Rodar a aplicação

```bash
# Desenvolvimento (com auto-reload)
./mvnw spring-boot:run

# Ou compilar e rodar o jar
./mvnw clean package -DskipTests
java -jar target/queue-system-0.0.1-SNAPSHOT.jar
```

### 4. Acessar

- **API:** `http://localhost:8080/api/`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

## Testes

```bash
# Todos os testes (unitários + integração)
./mvnw test

# Apenas testes unitários (mais rápido, usa H2 in-memory)
./mvnw test -Dtest="!**/integration/**"

# Apenas testes de integração
./mvnw test -Dtest="ChamadaRepositoryTest"
```

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/auth/login` | Autenticar com login/senha |
| POST | `/api/chamadas` | Criar nova chamada na fila |
| GET | `/api/chamadas` | Listar chamadas ativas |
| GET | `/api/chamadas/{id}` | Buscar chamada por ID |
| PUT | `/api/chamadas/{id}/chamar` | Chamar paciente para atendimento |
| PUT | `/api/chamadas/{id}/cancelar` | Cancelar chamada |
| PUT | `/api/chamadas/{id}/finalizar` | Finalizar atendimento |
| GET | `/api/chamadas/atendente/{id}` | Listar chamadas por atendente |
| GET | `/api/painel` | Status do painel |
| POST | `/api/profissionais` | Criar profissional |
| GET | `/api/profissionais` | Listar profissionais ativos |
| GET | `/api/relatorios/chamadas` | Gerar PDF de chamadas (filtro por data) |
| GET | `/api/relatorios/atendimentos` | Gerar PDF de atendimentos |
