# Spring Boot Project
Spring Boot Project for software training


## Documentando os endpoints:

### POST /auth/login

#### Request
```json
{
  "login": "String",
  "password": "String"
}
```

### POST /auth/register

#### Request
```json
{
  "login": "String",
  "name": "String",
  "password": "String",
  "role": "String"
}
```

### GET /api/UserServices/users

#### Response
```json
[
  {
    "id": "String",
    "login": "String",
    "name": "String"
  }
]
```

### GET /api/UserServices/users/{id}

#### Response
```json
{
  "id": "String",
  "login": "String",
  "name": "String"
}
```

### PUT /api/UserServices/users/{id}

#### Request
```json
{
  "login": "String",
  "name": "String"
}
```

### DELETE /api/UserServices/users/{id}

## Testes unitários
Para rodar os testes:
```shell
mvn test
```

### Testes

#### AuthenticantionControllerTest
- Caso 1: Testando caso de sucesso de login
- Caso 2: Testando caso de sucesso de cadastro
- Caso 3: Testando caso de fracasso de login

#### UserControllerTest
- Caso 1: Testando caso de sucesso em ler todos os usuários
- Caso 2: Testando caso de sucesso em ler um usuário
- Caso 3: Testando caso de sucesso em atualizar um usuário
- Caso 4: Testando caso de sucesso em deletar um usuário