# Spring Boot Project
Spring Boot Project for software training


## Documetando os endpoints:

### POST /auth/login

#### Request
```
{
  "login": String,
  "password": String
}
```

### POST /auth/register

#### Reqiest
```
{
  "login": String,
  "name": String,
  "password": String,
  "role": String
}
```

### GET /api/UserServices/users

#### Response
```
[
  {
    "id": String,
    "login": String,
    "name": String,
  }
]
```

### GET /api/UserServices/users/{id}

#### Response
```
{
  "id": String,
  "login": String,
  "name": String,
}
```

### PUT /api/UserServices/users/{id}

#### Request
```
{
  "login": String,
  "name": String,
}
```

### DELETE /api/UserServices/users/{id}
