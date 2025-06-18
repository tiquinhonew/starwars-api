# Star Wars API

API RESTful desenvolvida em Java 17 com Spring Boot que integra com a SWAPI (The Star Wars API) para gerenciar informações dos filmes da saga Star Wars.

## 🚀 Funcionalidades

- ✅ Carregamento automático dos filmes da SWAPI na inicialização
- ✅ Listagem de todos os filmes da saga
- ✅ Visualização de detalhes de filmes específicos
- ✅ Atualização de descrições dos filmes em memória
- ✅ Sistema de versionamento automático
- ✅ Tratamento de exceções personalizado
- ✅ Testes unitários e de integração completos

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.1.4**
- **Spring Web**
- **Spring Validation**
- **SpringDoc OpenAPI**
- **JUnit 5**
- **Mockito**
- **Jackson (JSON)**
- **Maven**

## 📋 Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Conexão com internet (para carregar dados da SWAPI)

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/tiquinhonew/starwars-api.git
cd starwars-api
```

### 2. Compile e execute os testes
```bash
mvn clean test
```

### 3. Execute a aplicação
```bash
mvn spring-boot:run
```

### 4. Acesse a API via Swagger UI
A aplicação estará disponível em: `http://localhost:8080/swagger-ui/index.html`

## 📚 Endpoints da API

### 📋 Listar todos os filmes
```http
GET /api/films
```

**Resposta:**
```json
[
  {
    "title": "A New Hope",
    "episode_id": 4,
    "opening_crawl": "It is a period of civil war...",
    "director": "George Lucas",
    "producer": "Gary Kurtz, Rick McCallum",
    "release_date": "1977-05-25",
    "version": 1,
    "customDescription": null,
    "lastModified": "2024-01-15T10:30:00"
  }
]
```

### 🎬 Detalhes de um filme específico
```http
GET /api/films/{episodeId}
```

**Exemplo:**
```bash
curl http://localhost:8080/api/films/4
```

### ✏️ Atualizar descrição de um filme
```http
PUT /api/films/{episodeId}/description
Content-Type: application/json

{
  "description": "Nova descrição do filme"
}
```

**Exemplo:**
```bash
curl -X PUT http://localhost:8080/api/films/4/description \
  -H "Content-Type: application/json" \
  -d '{"description": "Uma nova esperança para a galáxia..."}'
```

### 📊 Status da API
```http
GET /api/films/status
```

## 🧪 Executando os Testes

### Todos os testes
```bash
mvn test
```

### Apenas testes unitários
```bash
mvn test -Dtest="*Test"
```

## 🏗️ Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── dam
│   │           └── starwars
│   │               ├── StarWarsApiApplication.java             # Classe principal
│   │               ├── api
│   │               │   ├── client
│   │               │   ├── config
│   │               │   │   └── RestTemplateConfig.java         # Configuração HTTP
│   │               │   ├── controller
│   │               │   │   └── FilmController.java             # Endpoints REST
│   │               │   └── dto
│   │               ├── common
│   │               │   └── exception
│   │               │       ├── FilmNotFoundException.java      # Exceção customizada
│   │               │       └── GlobalExceptionHandler.java     # Tratamento global de erros
│   │               └── domain
│   │                   ├── model
│   │                   │   ├── Film.java                       # Modelo do filme
│   │                   │   ├── SwapiFilmResponse.java          # Resposta da SWAPI
│   │                   │   └── UpdateDescriptionRequest.java   # Request de atualização
│   │                   ├── repository
│   │                   └── service
│   │                       ├── FilmService.java                # Lógica de negócio dos filmes
│   │                       └── SwapiService.java               # Integração com SWAPI
│   └── resources
│       ├── application.yaml
└── test/java/com/dam/starwars                                  # Testes unitários e integração

```

## 🔧 Configurações

### application.yml
```yaml
server:
  port: 8080

swapi:
  base-url: https://swapi.py4e.com/api
  films-endpoint: /films/

logging:
  level:
    com.starwars.api: DEBUG
```

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🧑🏽‍💻 Douglas Moraes

Desenvolvido como parte do desafio técnico Star Wars API.

---

⭐ **May the Force be with you!** ⭐